package com.sorta.fsn.core.controller;

import com.sorta.fsn.common.exception.error.BaseBusinessModuleException;
import com.sorta.fsn.common.pojo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(annotations = Controller.class)
@Order(3)
public class GlobalExceptionController {


    Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);
    @ExceptionHandler(value = {RuntimeException.class})
    public String exceptionHandler(RuntimeException e, HttpServletResponse response) {
        logger.error(e.getMessage());
        return "error/error";
    }

}
