package com.forezp;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.RibbonHttpResponse;
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by forezp on 2017/4/8.
 */
@Component
public class RespFilter extends ZuulFilter{

    private static Logger log = LoggerFactory.getLogger(RespFilter.class);
    @Override
    public String filterType() {
        return "post";
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
        String startTimestamp =ctx.getZuulRequestHeaders().get("starttimestamp");
        long inTimes=-1;//无效值
        if(startTimestamp!=null){
            inTimes=System.currentTimeMillis()-Long.parseLong(startTimestamp);
        }
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response=ctx.getResponse();
        String targetServiceId=(String)ctx.get("serviceId");
        RibbonApacheHttpResponse ribbonApacheHttpResponse=(RibbonApacheHttpResponse)ctx.get("ribbonResponse");
        String targetInstance=null;
        if(ribbonApacheHttpResponse!=null) {
            targetInstance= ribbonApacheHttpResponse.getRequestedURI().toString();
        }
        response.addHeader("resp_timestamp",String.valueOf(new Date().getTime()));
        log.info(String.format("客户机: %s , 请求地址: %s , 方法: %s ,服务名: %s , 路由服务实例: %s , HTTP响应: %s，耗时: %sms", request.getRemoteAddr(),request.getRequestURL(),request.getMethod(), targetServiceId, targetInstance, response.getStatus(),inTimes));
        //log.info(String.format("targetServiceId: %s , targetInstance: %s ", targetServiceId,targetInstance));




        return null;
    }
}
