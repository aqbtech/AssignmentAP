package com.hcmutap.elearning.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.service.IStudentService;
import com.hcmutap.elearning.utils.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class StudentAPI {
	@Autowired
	private IStudentService studentService;
	@PostMapping("/student")
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		StudentModel studentModel = HttpUtil.of(request.getReader()).toModel(StudentModel.class);
		studentService.save(studentModel);
		mapper.writeValue(response.getOutputStream(), studentModel);
	}
	@PutMapping("/student")
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		StudentModel studentModel = HttpUtil.of(request.getReader()).toModel(StudentModel.class);
		studentService.update(studentModel);
		mapper.writeValue(response.getOutputStream(), studentModel);
	}
	@DeleteMapping("/student")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		StudentModel studentModel = HttpUtil.of(request.getReader()).toModel(StudentModel.class);
		studentService.delete(studentModel.getId());
		mapper.writeValue(response.getOutputStream(), "{}");
	}
}
