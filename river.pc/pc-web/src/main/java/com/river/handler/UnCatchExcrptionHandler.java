package com.river.handler;

import com.river.util.res.ResCodeEnum;
import com.river.util.res.WebDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;

/**
 * create by river  2018/5/16
 * desc:
 */
@ControllerAdvice
public class UnCatchExcrptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request,Exception e) {
        ModelAndView mav = null;
        //处理ajax请求
        if("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))){
            WebDTO<Object> dto = new WebDTO<Object>();
            mav = new ModelAndView(new MappingJackson2JsonView());
            dto.setResCode(ResCodeEnum.sys_error.getResCode());
            dto.setResMsg(e.getMessage());
        }else {
            mav = new ModelAndView("/error");
        }
        return mav;
    }
}
