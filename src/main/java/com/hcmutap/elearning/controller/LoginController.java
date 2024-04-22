package com.hcmutap.elearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String login() {
		return "login/NewloginForm";
	}
	@GetMapping("/accessDenied")
	public String accessDenied() {
		return "login/access_denied";
	}
}
