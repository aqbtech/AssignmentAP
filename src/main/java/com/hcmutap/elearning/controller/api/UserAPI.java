package com.hcmutap.elearning.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IUserService;
import com.hcmutap.elearning.utils.HttpUtil;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UserAPI {
	private static final Logger logger = LoggerFactory.getLogger(UserAPI.class);
	@Resource
	private IUserService userService;
	@PostMapping("/user")
	public String save(@RequestBody UserModel userModel) {
		try{
			return userService.save(userModel);
		}catch (Exception e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
			return null;
		}

	}
	@PutMapping("/user")
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		UserModel userModel = HttpUtil.of(request.getReader()).toModel(UserModel.class);
		try {
			userService.update(userModel);
		} catch (NotFoundException e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
		}
		mapper.writeValue(response.getOutputStream(), userModel);
	}
	@DeleteMapping("/user")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		UserModel userModel = HttpUtil.of(request.getReader()).toModel(UserModel.class);
		try {
			userService.delete(userModel.getFirebaseId());
		} catch (NotFoundException e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
		}
		mapper.writeValue(response.getOutputStream(), "{}");
	}
}
