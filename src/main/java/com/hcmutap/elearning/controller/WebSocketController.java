package com.hcmutap.elearning.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
	@MessageMapping("/account")
	@SendTo("/topic/account")
	public String account(String message) {
		return message;
	}
}
