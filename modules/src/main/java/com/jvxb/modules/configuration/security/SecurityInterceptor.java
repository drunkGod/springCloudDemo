package com.jvxb.modules.configuration.security;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 鉴权拦截器
 * @author lcl
 * @since 2019-09-10
 */
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = request.getHeader(SecurityConstant.AUTH_TOKEN);
        //请求必须携带token。
        if (StrUtil.isNotBlank(token)) {
            //如果token有效且未过期
            if (tokenProvider.checkToken(token)) {
                tokenProvider.refreshToken(token);
                SecurityUtil.setCurrentToken(token);
                return true;
            }
            //如果token错误，认为是已过期，需要重新登录。此时需要重新登录。
            String unAuth = String.format("{\"code\": %s, \"message\": \"%s\"}", SecurityConstant.UNAUTHORIZED, "UNAUTHORIZED");
            response.getOutputStream().print(unAuth);
            return false;
        }
        return false;
    }
}
