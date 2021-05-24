package com.xpp.zuul.filter;

import org.springframework.context.annotation.Configuration;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Configuration
public class RouteFilter extends ZuulFilter {
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.addZuulResponseHeader("abc", "Bearer " + "aaaaa");
		ctx.addOriginResponseHeader("abc", "Bearer " + "aaaaa");
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
    pre：可以在请求被路由之前调用
    routing：在路由请求时候被调用
    post：在routing和error过滤器之后被调用
    error：处理请求时发生错误时被调用
	 */
	@Override
	public String filterType() {
		return "route";
	}

	/**
	 * 通过int值来定义过滤器的执行顺序
	 */
	@Override
	public int filterOrder() {
		return 1;
	}

}
