package com.hcmutap.elearning.controller.api;


import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.service.IStudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentAPI {
	private static final Logger logger = LoggerFactory.getLogger(StudentAPI.class);
	@Autowired
	private IStudentService studentService;
	@PostMapping("/student")
	public void save(@RequestBody StudentModel studentModel){
		studentService.save(studentModel);
	}
	@PutMapping("/student")
	public void update(@RequestBody StudentModel studentModel) {
		try {
			studentService.update(studentModel);
		} catch (NotFoundException e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
		}
	}
	@DeleteMapping("/student")
	public void delete(@RequestBody List<String> ids) {
		try {
			studentService.delete(ids);
		} catch (NotFoundException e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
		}

	}
}
