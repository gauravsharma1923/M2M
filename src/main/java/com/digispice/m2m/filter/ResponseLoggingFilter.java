package com.digispice.m2m.filter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
@Order(1)
public class ResponseLoggingFilter extends OncePerRequestFilter {
	protected final static Log logger = LogFactory.getLog(ResponseLoggingFilter.class);
   

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      
    	if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
          
               filterChain.doFilter(request, response);
        }
        finally {
            afterRequest(request, response);
            response.copyBodyToResponse();
        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        
    	if (logger.isInfoEnabled()) 
        {
          
        	getResponsePayload(response);
        	logger.info("Response Payload["+getResponsePayload(response)+"]");
        }
    }

    private static String getResponsePayload(HttpServletResponse response) {
    	ContentCachingResponseWrapper wrapper =
				WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
    	
		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				int length = Math.min(buf.length,80000);
				try {
					
					return new String(buf, 0, length, wrapper.getCharacterEncoding());
					
				}
				catch (UnsupportedEncodingException ex) {
					logger.info("Response[Unknown]");
					return "[unknown]";
				}
			}
		}
		return null;
	}

   
    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}