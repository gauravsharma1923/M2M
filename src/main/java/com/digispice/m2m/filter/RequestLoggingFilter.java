package com.digispice.m2m.filter;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

@Component
@Order(2)
public class RequestLoggingFilter extends OncePerRequestFilter {

	public static final String DEFAULT_AFTER_MESSAGE_PREFIX = "Request Data [";

	public static final String DEFAULT_AFTER_MESSAGE_SUFFIX = "]";

	private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 1000000;
	
	private boolean includeQueryString = true;

	private boolean includeClientInfo = true;

	private boolean includeHeaders = true;

	private boolean includePayload = true;

	private int maxPayloadLength = DEFAULT_MAX_PAYLOAD_LENGTH;

	private String afterMessagePrefix = DEFAULT_AFTER_MESSAGE_PREFIX;

	private String afterMessageSuffix = DEFAULT_AFTER_MESSAGE_SUFFIX;


	public void setIncludeQueryString(boolean includeQueryString) {
		this.includeQueryString = includeQueryString;
	}


	protected boolean isIncludeQueryString() {
		return this.includeQueryString;
	}


	public void setIncludeClientInfo(boolean includeClientInfo) {
		this.includeClientInfo = includeClientInfo;
	}

	
	protected boolean isIncludeClientInfo() {
		return this.includeClientInfo;
	}

	
	public void setIncludeHeaders(boolean includeHeaders) {
		this.includeHeaders = includeHeaders;
	}

	
	public boolean isIncludeHeaders() {
		return this.includeHeaders;
	}

	
	public void setIncludePayload(boolean includePayload) {
		this.includePayload = includePayload;
	}

	protected boolean isIncludePayload() {
		return this.includePayload;
	}

	
	public void setMaxPayloadLength(int maxPayloadLength) {
		Assert.isTrue(maxPayloadLength >= 0, "'maxPayloadLength' should be larger than or equal to 0");
		this.maxPayloadLength = maxPayloadLength;
	}

	
	protected int getMaxPayloadLength() {
		return this.maxPayloadLength;
	}

	public void setAfterMessagePrefix(String afterMessagePrefix) {
		this.afterMessagePrefix = afterMessagePrefix;
	}

	public void setAfterMessageSuffix(String afterMessageSuffix) {
		this.afterMessageSuffix = afterMessageSuffix;
	}

	@Override
	protected boolean shouldNotFilterAsyncDispatch() {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		boolean isFirstRequest = !isAsyncDispatch(request);
		HttpServletRequest requestToUse = request;

		if (isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
			requestToUse = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
		}
		
		boolean shouldLog = shouldLog(requestToUse);
		try {
			filterChain.doFilter(requestToUse, response);
		}
		finally {
			if (shouldLog && !isAsyncStarted(requestToUse)) {
				afterRequest(requestToUse, getAfterMessage(requestToUse));
				logger.info(afterRequest(requestToUse, getAfterMessage(requestToUse)));
			}
		}
	}

	private String getAfterMessage(HttpServletRequest request) {
		return createMessage(request, this.afterMessagePrefix, this.afterMessageSuffix);
	}

	
	protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
		
		StringBuilder message = new StringBuilder();
		
		message.append(prefix);
		
		message.append("uri=").append(request.getRequestURI());
		
		String requestMethod=request.getMethod();
		
		message.append(";method=").append(requestMethod);

		if (isIncludeQueryString()) 
		{
			
			String queryString = request.getQueryString();
			
			if (queryString != null)
			{
				message.append('?').append(queryString);
			}
		}

		if (isIncludeClientInfo()) {
			String client = request.getRemoteAddr();
			if (StringUtils.hasLength(client)) {
				message.append(";client=").append(client);
			}
			HttpSession session = request.getSession(false);
			if (session != null) {
				message.append(";session=").append(session.getId());
			}
			String user = request.getRemoteUser();
			if (user != null) {
				message.append(";user=").append(user);
			}
		}

		if (isIncludeHeaders()) {
			message.append(";headers=").append(new ServletServerHttpRequest(request).getHeaders());
		}

		if (isIncludePayload()) {
			String payload = getMessagePayload(request);
			if (payload != null) {
				message.append(";payload=").append(payload);
			}
		}

		message.append(suffix);
		
		return message.toString();
	}

	
	@Nullable
	protected String getMessagePayload(HttpServletRequest request) {
		ContentCachingRequestWrapper wrapper =
				WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				int length = Math.min(buf.length, getMaxPayloadLength());
				try {
					return new String(buf, 0, length, wrapper.getCharacterEncoding());
				}
				catch (UnsupportedEncodingException ex) {
					return "[unknown]";
				}
			}
		}
		return null;
	}

	protected boolean shouldLog(HttpServletRequest request) {
		return true;
	}


	protected  String afterRequest(HttpServletRequest request, String message)
	{
		return message;
	}

}
