package com.sorta.fsn.core.interceptor;

import com.sorta.fsn.common.exception.error.BaseBusinessModuleException;
import com.sorta.fsn.common.exception.error.DefaultError;
import com.sorta.fsn.common.pojo.BaseResponse;
import com.sorta.fsn.common.utils.SignatureGenerator;
import com.sorta.fsn.core.controller.GlobalExceptionRestController;
import com.sorta.fsn.core.security.annotation.IgnoreSignChecking;
import com.sorta.fsn.core.security.annotation.IgnoreTokenChecking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Configuration
public class CheckSignInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(CheckSignInterceptor.class);

    @Value("${fsn.secrety.secretKey}")
    private String secretKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Method method = handlerMethod.getMethod();
        if(method.getAnnotationsByType(IgnoreSignChecking.class).length>0){
            return true;
        }

        String sign = request.getParameter("sign");
        // 如果参数中不包含sign参数，则直接返回签名校验错误
        if (StringUtils.isEmpty(sign)) {
            throw new BaseBusinessModuleException(DefaultError.SIGN_NOT_FOUND);
        }
        String requestUri = request.getRequestURI().substring(1);// 去掉第一个斜杠(/)
        // 获取参数列表
        Map<String, String> reqParams = new HashMap<>();
        Map<String, String[]> primaryParams = getQueryParam(request);
        Iterator it = primaryParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> e = (Map.Entry) it.next();
            reqParams.put(e.getKey(), e.getValue()[0]);
        }

        //如果sign不相等
        if(!sign.equals( SignatureGenerator.generate(requestUri, reqParams, secretKey))){
            throw new BaseBusinessModuleException(DefaultError.INVALID_SIGN);
        }

        return true;
    }

    private Map<String, String[]> getQueryParam(HttpServletRequest request) {
        Map<String, String[]> result = new HashMap<>();
        String queryString = request.getQueryString();
        if (StringUtils.isEmpty(queryString))
            return result;
        try {
            queryString = URLDecoder.decode(queryString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("url decode query string failed!\n", e);
            return result;
        }
        String[] paramWithValues = queryString.split("&");
        for (String paramWithValue : paramWithValues) {
            String[] paramAndValue = paramWithValue.split("=");
            if(!"sign".equals(paramAndValue[0])) {
                if("token".equals(paramAndValue[0])){
                    try {
                        paramAndValue[1] = URLEncoder.encode(paramAndValue[1],"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        logger.error(e.getMessage());
                    }
                }
                result.put(paramAndValue[0], new String[]{paramAndValue.length > 1 ? paramAndValue[1] : ""});
            }
        }

        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String appkey="5732477868";
        String sk = "94a7cbbf8511a288d22d4cf8705d61d0";
        String email = "test";
        String token = "UAbmitU4cA59dsCMNxRHdo7E7Y%2FCDBTO%2BvwXFwpHL6wgn3j5Onhr9wdu8Mn0AsFu";
        String uri = "api/user/getByEmail";

        Map<String, String> params = new HashMap<String, String>();
       // params.put("appkey", appkey);
        params.put("email", email);
        params.put("token", token);
        String sign = SignatureGenerator.generate(uri, params, sk);

        System.out.println(sign);
    }

}
