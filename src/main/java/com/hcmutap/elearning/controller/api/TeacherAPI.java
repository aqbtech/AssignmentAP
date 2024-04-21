package com.hcmutap.elearning.controller.api;


import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.ITeacherService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TeacherAPI {
	private static final Logger logger = LoggerFactory.getLogger(TeacherAPI.class);
	@Resource(name="teacherService")
	private ITeacherService teacherService;
	@GetMapping("/teacher/findAll")
	public List<TeacherModel> findAll() {
		return teacherService.findAll();
	}
	@GetMapping("/teacher/findById")
	public TeacherModel findById(@RequestParam String id) {
		try {
			return teacherService.findById(id);
		} catch (NotFoundException e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
			return null;
		}
	}
	@PostMapping("/teacher")
	public String save(@RequestBody TeacherModel teacherModel) {
		return teacherService.save(teacherModel);
	}
	@PutMapping("/teacher")
	public void update(@RequestBody TeacherModel teacherModel) {
		try {
			teacherService.update(teacherModel);
		} catch (NotFoundException e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
		}
	}
	@DeleteMapping("/teacher")
	public void delete(@RequestBody List<String> ids) {
		try {
			teacherService.delete(ids);
		} catch (NotFoundException e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
		}
	}
}
