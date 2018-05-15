package com.river.handler;

import com.alibaba.fastjson.JSONObject;
import com.river.util.exception.ServiceException;
import com.river.util.res.ResCodeEnum;
import com.river.util.res.WebDTO;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.validator.constraints.Mod11Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * create by river  2018/5/16
 * desc:
 */
@Component
@Aspect
public class WebControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebControllerExceptionHandler.class);

    /**
     * 定义一个切点
     */
    @Pointcut("execution(* com.river.controller..*.*(..))")
    public void controllerPoint() {

    }

    @Around("controllerPoint() && @annotation(org.springframework.web.bind.annotation.ResponseBody)")
    public String doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("切面【doAroundAdvice】执行的方法【" + proceedingJoinPoint.getSignature().getName() +"】");
        WebDTO<Object> dto = new WebDTO<Object>();
        String result = null;
        try{
            /**
             * 执行目标方法
             */
            result = (String) proceedingJoinPoint.proceed();
        }catch (ServiceException e) {
            logger.error("业务异常，异常信息",e);
            dto.setResCode(e.getCode());
            dto.setResMsg(e.getMessage());
        }catch (Exception e) {
            logger.error("系统异常，异常信息",e);
            dto.setResEnum(ResCodeEnum.sys_error);
        }
        return StringUtils.isBlank(result) ? JSONObject.toJSONString(dto):result;
    }
}
