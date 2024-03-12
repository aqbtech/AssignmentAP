package com.hcmutap.elearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String login() {
		return "login/login_form";
	}
	@PostMapping("/login")
	public String login(String username, String password) {
		System.out.println(username + " " + password);
		return "login/login_success";
	}
}
