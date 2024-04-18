package com.hcmutap.elearning.controller.api;

import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.service.IRegisterService;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class RegisterAPI {
	private static final Logger logger = LoggerFactory.getLogger(RegisterAPI.class);
	@Resource
	private IRegisterService registerService;
	@PostMapping("/register")
	public void register(@RequestBody ModelMap model) {
		try {
			registerService.register(model);
		} catch (NotFoundException e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
		}
	}

}
