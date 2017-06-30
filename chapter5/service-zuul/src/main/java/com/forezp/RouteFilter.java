package com.forezp;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by forezp on 2017/4/8.
 */
@Component
public class RouteFilter extends ZuulFilter{

    private static Logger log = LoggerFactory.getLogger(RouteFilter.class);
    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        ctx.addZuulRequestHeader("starttimestamp",String.valueOf(System.currentTimeMillis()));
       // log.info(String.format("client: %s , request: %s , routeUrl: %s", request.getRemoteAddr(),request.getRequestURL(),ctx.getRouteHost()));
        return null;
    }
}
