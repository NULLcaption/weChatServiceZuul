package com.xpp.zuul.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Configuration
public class AccessFilter extends ZuulFilter {
	
	@Value("${oauth.tokenName:xpp_access_token}")
	private String tokenName;
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if (request.getCookies() == null) return null;
        for (Cookie cookie: request.getCookies()) {
        	if (tokenName.equals(cookie.getName())) {
        		ctx.addZuulRequestHeader("Authorization", "Bearer " + cookie.getValue());
        		break;
        	}
        }
        return null;
	}

	/**
	 * 返回一个boolean类型来判断该过滤器是否要执行
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * 返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型
	 * pre：可以在请求被路由之前调用
	 * route：在路由请求时候被调用
	 * post：在routing和error过滤器之后被调用
	 * error：处理请求时发生错误时被调用
	 */
	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	/**
	 * 通过int值来定义过滤器的执行顺序
	 */
	@Override
	public int filterOrder() {
		return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
	}

}
