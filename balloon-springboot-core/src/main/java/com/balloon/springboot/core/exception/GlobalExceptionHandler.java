package com.balloon.springboot.core.exception;

import com.balloon.core.exception.BusinessRuntimeException;
import com.balloon.springboot.core.rules.ResultVO;
import com.balloon.springboot.core.rules.ResultVOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常拦截
 * <p>全局拦截 Controller 异常, 校验是否是 BusinessRuntimeException 业务异常, 是则将这个业务异常返回给前端</p>
 * <p>如果不是 BusinessRuntimeException 业务异常, 则默认抛出【系统异常】给前端</p>
 *
 * @author liaofuxing
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResultVO<?> defaultErrorHandler(HttpServletRequest request, Exception e) {

        logger.info("全局异常拦截...");
        logger.info("url: {}, msg: {}", request.getRequestURI(), e.getMessage());
        ResultVO<Object> resultVO = new ResultVO<>();

        if (e instanceof BusinessRuntimeException) {
            BusinessRuntimeException businessRuntimeException = (BusinessRuntimeException) e;
            resultVO.setCode(businessRuntimeException.getCode());
            resultVO.setMsg(businessRuntimeException.getMessage());
        } else {
            resultVO = ResultVOUtils.error(null);
        }
        logger.error("ERROR: " + e.getMessage());
        return resultVO;
    }
}
