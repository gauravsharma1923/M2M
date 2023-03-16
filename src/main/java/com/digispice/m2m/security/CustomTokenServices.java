package com.digispice.m2m.security;
import com.digispice.m2m.exception.models.InvalidGrantException;
import com.digispice.m2m.exception.models.InvalidTokenException;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

public class CustomTokenServices implements AuthorizationServerTokenServices, ResourceServerTokenServices, ConsumerTokenServices, InitializingBean
{
  
  private int refreshTokenValiditySeconds = 2592000;
  
  private int accessTokenValiditySeconds = 43200;
  
  private boolean supportRefreshToken = false;
  
  private boolean reuseRefreshToken = true;
  
  private TokenStore tokenStore;
  
  private ClientDetailsService clientDetailsService;
  
  private TokenEnhancer accessTokenEnhancer;
  
  private AuthenticationManager authenticationManager;

  
  public void afterPropertiesSet() throws Exception
  {
		Assert.notNull(tokenStore, "tokenStore must be set");
  }

    @Transactional
	public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {

		OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
		OAuth2RefreshToken refreshToken = null;
		if (existingAccessToken != null) {
			if (existingAccessToken.isExpired()) {
				if (existingAccessToken.getRefreshToken() != null) {
					refreshToken = existingAccessToken.getRefreshToken();
					
					tokenStore.removeRefreshToken(refreshToken);
				}
				tokenStore.removeAccessToken(existingAccessToken);
			}
			else {
				
				tokenStore.storeAccessToken(existingAccessToken, authentication);
				return existingAccessToken;
			}
		}

		
		if (refreshToken == null) {
			refreshToken = createRefreshToken(authentication);
		}
		
		else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
			ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
			if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
				refreshToken = createRefreshToken(authentication);
			}
		}

		OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
		tokenStore.storeAccessToken(accessToken, authentication);
		
		refreshToken = accessToken.getRefreshToken();
		if (refreshToken != null) {
			tokenStore.storeRefreshToken(refreshToken, authentication);
		}
		return accessToken;

	}

  
    @Transactional(noRollbackFor={InvalidTokenException.class, InvalidGrantException.class})
	public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest)
			throws AuthenticationException {

		if (!supportRefreshToken) {
			throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
		}

		OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
		if (refreshToken == null) {
			throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
		}

		OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
		if (this.authenticationManager != null && !authentication.isClientOnly()) {
			
			Authentication user = new PreAuthenticatedAuthenticationToken(authentication.getUserAuthentication(), "", authentication.getAuthorities());
			user = authenticationManager.authenticate(user);
			Object details = authentication.getDetails();
			authentication = new OAuth2Authentication(authentication.getOAuth2Request(), user);
			authentication.setDetails(details);
		}
		String clientId = authentication.getOAuth2Request().getClientId();
		if (clientId == null || !clientId.equals(tokenRequest.getClientId())) {
			throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
		}

		
		tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);

		if (isExpired(refreshToken)) {
			tokenStore.removeRefreshToken(refreshToken);
			throw new InvalidTokenException("Invalid refresh token (expired): " + refreshToken);
		}

		authentication = createRefreshedAuthentication(authentication, tokenRequest);

		if (!reuseRefreshToken) {
			tokenStore.removeRefreshToken(refreshToken);
			refreshToken = createRefreshToken(authentication);
		}

		OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
		tokenStore.storeAccessToken(accessToken, authentication);
		if (!reuseRefreshToken) {
			tokenStore.storeRefreshToken(accessToken.getRefreshToken(), authentication);
		}
		return accessToken;
	}

  
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		return tokenStore.getAccessToken(authentication);
	}

    
	private OAuth2Authentication createRefreshedAuthentication(OAuth2Authentication authentication, TokenRequest request) {
		OAuth2Authentication narrowed = authentication;
		Set<String> scope = request.getScope();
		OAuth2Request clientAuth = authentication.getOAuth2Request().refresh(request);
		if (scope != null && !scope.isEmpty()) {
			Set<String> originalScope = clientAuth.getScope();
			if (originalScope == null || !originalScope.containsAll(scope)) {
				throw new InvalidScopeException("Unable to narrow the scope of the client authentication to " + scope
						+ ".", originalScope);
			}
			else {
				clientAuth = clientAuth.narrowScope(scope);
			}
		}
		narrowed = new OAuth2Authentication(clientAuth, authentication.getUserAuthentication());
		return narrowed;
	}

	protected boolean isExpired(OAuth2RefreshToken refreshToken) {
		if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
			ExpiringOAuth2RefreshToken expiringToken = (ExpiringOAuth2RefreshToken) refreshToken;
			return expiringToken.getExpiration() == null
					|| System.currentTimeMillis() > expiringToken.getExpiration().getTime();
		}
		return false;
	}
  
	public OAuth2AccessToken readAccessToken(String accessToken) {
		return tokenStore.readAccessToken(accessToken);
	}

	public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException,
			InvalidTokenException {
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
		if (accessToken == null) {
			throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
		}
		else if (accessToken.isExpired()) {
			tokenStore.removeAccessToken(accessToken);
			throw new InvalidTokenException("Access token expired: " + accessTokenValue);
		}

		OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
		if (result == null) {
			
			throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
		}
		if (clientDetailsService != null) {
			String clientId = result.getOAuth2Request().getClientId();
			try {
				clientDetailsService.loadClientByClientId(clientId);
			}
			catch (ClientRegistrationException e) {
				throw new InvalidTokenException("Client not valid: " + clientId, e);
			}
		}
		return result;
	}

	public String getClientId(String tokenValue) {
		OAuth2Authentication authentication = tokenStore.readAuthentication(tokenValue);
		if (authentication == null) {
			throw new InvalidTokenException("Invalid access token: " + tokenValue);
		}
		OAuth2Request clientAuth = authentication.getOAuth2Request();
		if (clientAuth == null) {
			throw new InvalidTokenException("Invalid access token (no client id): " + tokenValue);
		}
		return clientAuth.getClientId();
	}

	public boolean revokeToken(String tokenValue) {
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
		if (accessToken == null) {
			return false;
		}
		if (accessToken.getRefreshToken() != null) {
			tokenStore.removeRefreshToken(accessToken.getRefreshToken());
		}
		tokenStore.removeAccessToken(accessToken);
		return true;
	}

	private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication) {
		if (!isSupportRefreshToken(authentication.getOAuth2Request())) {
			return null;
		}
		int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
		String value = UUID.randomUUID().toString();
		if (validitySeconds > 0) {
			return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis()
					+ (validitySeconds * 1000L)));
		}
		return new DefaultOAuth2RefreshToken(value);
	}

	private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
		DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
		int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
		if (validitySeconds > 0) {
			token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
		}
		token.setRefreshToken(refreshToken);
		token.setScope(authentication.getOAuth2Request().getScope());

		return accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token, authentication) : token;
	}

	
	protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
		if (clientDetailsService != null) {
			ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
			Integer validity = client.getAccessTokenValiditySeconds();
			if (validity != null) {
				return validity;
			}
		}
		return accessTokenValiditySeconds;
	}

	
	protected int getRefreshTokenValiditySeconds(OAuth2Request clientAuth) {
		if (clientDetailsService != null) {
			ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
			Integer validity = client.getRefreshTokenValiditySeconds();
			if (validity != null) {
				return validity;
			}
		}
		return refreshTokenValiditySeconds;
	}

	
	protected boolean isSupportRefreshToken(OAuth2Request clientAuth) {
		if (clientDetailsService != null) {
			ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
			return client.getAuthorizedGrantTypes().contains("refresh_token");
		}
		return this.supportRefreshToken;
	}

	
	public void setTokenEnhancer(TokenEnhancer accessTokenEnhancer) {
		this.accessTokenEnhancer = accessTokenEnhancer;
	}

	
	public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}

	
	public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	
	public void setSupportRefreshToken(boolean supportRefreshToken) {
		this.supportRefreshToken = supportRefreshToken;
	}

	
	public void setReuseRefreshToken(boolean reuseRefreshToken) {
		this.reuseRefreshToken = reuseRefreshToken;
	}

	
	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}
}
