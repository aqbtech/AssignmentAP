package com.hcmutap.elearning.controller;

import com.hcmutap.elearning.exception.CustomRuntimeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class is used to handle the global exception for the application.
 */
@ControllerAdvice
public class GlobalHandlerCRE {
	/**
	 * This method is used to handle the global exception for CustomRuntimeException of the application.
	 */
	@ExceptionHandler(CustomRuntimeException.class)
	public ModelAndView handleCustomRuntimeException(CustomRuntimeException ex) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login/Rare_fault"); // name of your error page
		modelAndView.addObject("message", ex.getMessage());
		modelAndView.addObject("code", ex.getCode());
		return modelAndView;
	}
}
