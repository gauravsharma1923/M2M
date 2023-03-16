 package com.digispice.m2m.config;
 import com.digispice.m2m.security.CustomTokenServices;
 import com.digispice.m2m.security.CustomUserDetailsService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.context.annotation.Primary;
 import org.springframework.http.HttpMethod;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
 import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
 import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
 import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
 import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
 import org.springframework.security.oauth2.provider.token.TokenStore;
 import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
 import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


 @Configuration
 @EnableAuthorizationServer
 public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
 {
   @Autowired
   private AuthenticationManager authenticationManager;
   
   @Autowired
   private CustomUserDetailsService userDetailsService;
   
   @Value("${signing-key:oui214hmui23o4hm1pui3o2hp4m1o3h2m1o43}")
   private String signingKey;
   
   @Autowired
   private PasswordEncoder passwordEncoder;

   public AuthorizationServerConfiguration() {
        super();
   }

   @Bean
   public JwtAccessTokenConverter accessTokenConverter() {
     final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
     jwtAccessTokenConverter.setSigningKey(this.signingKey);
     jwtAccessTokenConverter.setVerifierKey(this.signingKey);
     return jwtAccessTokenConverter;
   }


   @Bean
   @Primary
   public CustomTokenServices tokenServices() {
     CustomTokenServices tokenServices = new CustomTokenServices();
     tokenServices.setSupportRefreshToken(true);
     tokenServices.setTokenStore(tokenStore());
     return tokenServices;
   }



   @Bean
   public TokenStore tokenStore()
   {

	   return new JwtTokenStore(accessTokenConverter());
   }

   @Override
   public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
       .withClient("digispice")
       .secret(passwordEncoder.encode("RCFnISRwaWNlIUAjQCQ0ODVA"))
       .authorizedGrantTypes("password", "refresh_token")
       .scopes("read", "write" )
       .refreshTokenValiditySeconds(3600*24)
       .autoApprove( "m2m")
       .accessTokenValiditySeconds(3600);
   }

   @Override
   public void configure(final AuthorizationServerEndpointsConfigurer endpoints)
   {
	    endpoints
       .tokenStore(tokenStore())
       .authenticationManager(authenticationManager)
       .userDetailsService(userDetailsService)
       .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE)
       .accessTokenConverter(accessTokenConverter());
   }



   @Override
   public void configure(final AuthorizationServerSecurityConfigurer security) throws Exception {
     security.checkTokenAccess("isAuthenticated()");
     security.tokenKeyAccess("permitAll()");
     super.configure(security);
   }
 }





