package com.sorta.fsn.core.interceptor;
import com.sorta.fsn.common.exception.error.BaseBusinessModuleException;
import com.sorta.fsn.common.exception.error.DefaultError;
import com.sorta.fsn.core.security.annotation.IgnoreTokenChecking;
import com.sorta.fsn.core.security.user.Token;
import com.sorta.fsn.core.security.user.token.EncryptedToken;
import com.sorta.fsn.core.security.user.token.TokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


public class  CheckTokenInterceptor extends HandlerInterceptorAdapter {

    @Value("${fsn.secrety.secretKey}")
    private String secretKey;
    @Value("${fsn.secrety.algorithm:DES}")
    private String algorithm;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Method method = handlerMethod.getMethod();
         if(method.getAnnotationsByType(IgnoreTokenChecking.class).length>0){
             return true;
         }

        String token = request.getParameter("token");
        if(StringUtils.isEmpty(token)){
            throw new BaseBusinessModuleException(DefaultError.TOKEN_NOT_FOUND);
        }


        Token t = new EncryptedToken(secretKey, algorithm, token);
        TokenUtil.setToken(t);
        return true;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
