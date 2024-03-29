package com.hcmutap.elearning.controller.api;


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
		return teacherService.findById(id);
	}
	@PostMapping("/teacher")
	public String save(@RequestBody TeacherModel teacherModel) {
		return teacherService.save(teacherModel);
	}
	@PutMapping("/teacher")
	public void update(@RequestBody TeacherModel teacherModel) {
		teacherService.update(teacherModel);
	}
	@DeleteMapping("/teacher")
	public void delete(@RequestBody List<String> ids) {
		teacherService.delete(ids);
	}
}
