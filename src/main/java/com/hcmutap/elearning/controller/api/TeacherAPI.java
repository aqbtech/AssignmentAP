package com.hcmutap.elearning.controller.api;


import com.hcmutap.elearning.exception.CustomRuntimeException;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.exception.TransactionalException;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.ITeacherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TeacherAPI {
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
			throw new RuntimeException(e);
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
			throw new CustomRuntimeException(e.getMessage(), "404");
		} catch (TransactionalException e) {
			throw new CustomRuntimeException(e.getMessage(), "100");
		}
	}
	@DeleteMapping("/teacher")
	public void delete(@RequestBody List<String> ids) {
		try {
			teacherService.delete(ids);
		} catch (NotFoundException e) {
			throw new CustomRuntimeException(e.getMessage(), "404");
		} catch (TransactionalException e) {
			throw new CustomRuntimeException(e.getMessage(), "100");
		}
	}
}
