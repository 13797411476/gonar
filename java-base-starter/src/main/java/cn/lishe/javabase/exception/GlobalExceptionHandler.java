package cn.lishe.javabase.exception;

import cn.lishe.javabase.define.Re;
import cn.lishe.javabase.define.Response;
import cn.lishe.javabase.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Connor
 * @date 2019/5/13 16:00
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * ServletRequestBindingException
     */
    @ExceptionHandler
    @ResponseBody
    public Response handleBindException(ServletRequestBindingException e) {
        log.warn(e.getMessage(), e);
        return R.write(Re.PARAM_ERROR);
    }

    /**
     * 参数错误异常捕获
     */
    @ExceptionHandler
    @ResponseBody
    public Response handleBindException(BindException e) {
        log.warn(e.getBindingResult().toString(), e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError param : fieldErrors) {
            sb.append(param.getField()).append(":").append(param.getDefaultMessage()).append(",");
        }
        return R.write(Re.PARAM_ERROR, sb.toString());
    }

    @ExceptionHandler
    @ResponseBody
    public Response handleBusinessException(BusinessException e) {
        log.error(e.getResultCode().getMsg(), e);
        return R.write(e.getResultCode());
    }

    @ExceptionHandler
    @ResponseBody
    public Response handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return R.error();
    }

    @ExceptionHandler
    @ResponseBody
    public Response handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error();
    }

}
