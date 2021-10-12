package com.camille.seckill.exception;

import com.camille.seckill.vo.RespBean;
import com.camille.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e){
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException) e;
            return RespBean.error(ex.getRespBeanEnum());
        } else if(e instanceof BindException){
            BindException ex = (BindException) e;
            RespBean error = RespBean.error(RespBeanEnum.BIND_ERROR);
            error.setMessage("参数效验异常：" + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return error;
        }
        return RespBean.error(RespBeanEnum.ERROR);
    }

}
