package com.xpp.zuul.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Configuration
public class PostFilter extends ZuulFilter {
	
	private static Logger log = LoggerFactory.getLogger(PostFilter.class);
	
	@Value("${home:}")
	private String home;
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletResponse response = ctx.getResponse();
		if (response.getStatus() == 401) {
			sendRedirect(response, home);
		}
        return null;
	}
	
	private void sendRedirect(HttpServletResponse response, String redirectUrl){
        try {
            response.setHeader(HttpHeaders.LOCATION, redirectUrl);
            response.setStatus(HttpStatus.FOUND.value());
            response.flushBuffer();
        } catch (IOException ex) {
        	log.error("Could not redirect to: " + redirectUrl, ex);
        }
    }

	/**
	 * 返回一个boolean类型来判断该过滤器是否要执行
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public String filterType() {
		return FilterConstants.POST_TYPE;
	}

	@Override
	public int filterOrder() {
		return -1;
	}

}
