package com.hcmutap.elearning.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.ITeacherService;
import com.hcmutap.elearning.utils.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TeacherAPI {
	@Autowired
	private ITeacherService teacherService;
	@PostMapping("/teacher")
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		TeacherModel teacherModel = HttpUtil.of(request.getReader()).toModel(TeacherModel.class);
		teacherService.save(teacherModel);
		mapper.writeValue(response.getOutputStream(), teacherModel);
	}
	@PutMapping("/teacher")
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		TeacherModel teacherModel = HttpUtil.of(request.getReader()).toModel(TeacherModel.class);
		teacherService.update(teacherModel);
		mapper.writeValue(response.getOutputStream(), teacherModel);
	}
	@DeleteMapping("/teacher")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		TeacherModel teacherModel = HttpUtil.of(request.getReader()).toModel(TeacherModel.class);
		teacherService.delete(teacherModel.getId());
		mapper.writeValue(response.getOutputStream(), "{}");
	}
}
