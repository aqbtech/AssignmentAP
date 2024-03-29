package com.hcmutap.elearning.controller.api;

import com.hcmutap.elearning.service.IRegisterService;

import jakarta.annotation.Resource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class RegisterAPI {
	@Resource
	private IRegisterService registerService;
	@PostMapping("/register")
	public void register(@RequestBody ModelMap model) {
		registerService.register(model);
	}

}
