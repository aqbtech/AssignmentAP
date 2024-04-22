package com.hcmutap.elearning.controller;

import com.hcmutap.elearning.constant.SystemConstant;
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
		if (SystemConstant.NOT_FOUND.equals(ex.getCode())) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("login/404_page"); // name of your error page
			modelAndView.addObject("message", ex.getMessage());
			modelAndView.addObject("code", ex.getCode());
			return modelAndView;
		} else if (SystemConstant.FUNC_PERFORM_ERROR.equals(ex.getCode())) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("login/Rare_fault"); // name of your error page
			modelAndView.addObject("message", ex.getMessage());
			modelAndView.addObject("code", ex.getCode());
			return modelAndView;
		} else if (SystemConstant.REGISTER_SERVICE_ERROR.equals(ex.getCode())) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("admin/views/createAccount"); // name of your error page
			modelAndView.addObject("message", ex.getMessage());
			modelAndView.addObject("code", ex.getCode());
			return modelAndView;
		} else if (SystemConstant.REGISTER_BY_FILE_ERROR.equals(ex.getCode())) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("admin/views/add-many-account"); // name of your error page
			modelAndView.addObject("message", ex.getMessage());
			modelAndView.addObject("code", ex.getCode());
			return modelAndView;
		} else if (SystemConstant.DATABASE_ERROR.equals(ex.getCode())) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("login/Rare_fault"); // name of your error page
			modelAndView.addObject("message", ex.getMessage());
			modelAndView.addObject("code", ex.getCode());
			return modelAndView;
		} else if (SystemConstant.MAPPING_EXCEPTION.equals(ex.getCode())) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("login/Rare_fault"); // name of your error page
			modelAndView.addObject("message", ex.getMessage());
			modelAndView.addObject("code", ex.getCode());
			return modelAndView;
		} else {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("login/Rare_fault"); // name of your error page
			modelAndView.addObject("message", ex.getMessage());
			modelAndView.addObject("code", ex.getCode());
			return modelAndView;
		}
	}
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login/Rare_fault"); // name of your error page
		modelAndView.addObject("message", ex.getMessage());
		modelAndView.addObject("code", 500);
		return modelAndView;
	}
}
