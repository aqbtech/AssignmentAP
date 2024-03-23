package com.hcmutap.elearning.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.ITeacherService;
import com.hcmutap.elearning.utils.HttpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TeacherAPI {
	@Resource
	private ITeacherService teacherService;
	@GetMapping("/teacher/findAll")
	public List<TeacherModel> findAll() {
		return teacherService.findAll();
	}
	@GetMapping("/teacher/findById")
	public TeacherModel findById(@RequestParam String id) {
		return teacherService.findById(id);
	}
	@PostMapping("/teacher")
	public String save(@RequestBody TeacherModel teacherModel) {
		return teacherService.save(teacherModel);
	}
	@PutMapping("/teacher")
	public void update(@RequestBody TeacherModel teacherModel) {
		// need to handler not found exception in dao -> service -> controller
		teacherService.update(teacherModel);
	}
	@DeleteMapping("/teacher")
	public void delete(@RequestBody List<String> ids) {
		ids.forEach(id -> teacherService.delete(id));
	}
}
