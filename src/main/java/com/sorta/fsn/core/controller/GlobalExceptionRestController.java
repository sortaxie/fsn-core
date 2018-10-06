package com.sorta.fsn.core.controller;

import com.sorta.fsn.common.exception.error.BaseBusinessModuleException;
import com.sorta.fsn.common.pojo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletResponse;

/**
 * @author benju.xie
 * @version 0.0.1
 * @datetime 2017/5/10 17:41
 */
@ControllerAdvice(annotations = RestController.class)
@Order(2)
public class GlobalExceptionRestController {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionRestController.class);
    @ExceptionHandler(value = {RuntimeException.class, BaseBusinessModuleException.class})
    @ResponseBody //在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public BaseResponse exceptionHandler(RuntimeException e, HttpServletResponse response) {
        BaseResponse resp = null;
        if (e instanceof BaseBusinessModuleException) {
            logger.error("GlobalException:"+e.getMessage());
            resp = new BaseResponse(((BaseBusinessModuleException) e).getError());
        } else {
            resp = new BaseResponse();
            resp.setStatus(BaseResponse.Status.FAILED);
            resp.setErrorMessage(e.getMessage());
        }

        return resp;
    }
}
