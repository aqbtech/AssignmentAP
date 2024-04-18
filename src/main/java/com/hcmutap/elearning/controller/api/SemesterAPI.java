package com.hcmutap.elearning.controller.api;

import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.SemesterModel;
import com.hcmutap.elearning.service.impl.SemesterService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SemesterAPI {
	private static final Logger logger = LoggerFactory.getLogger(SemesterAPI.class);
	private final SemesterService semesterService;
	@Autowired
	public SemesterAPI(SemesterService semesterService) {
		this.semesterService = semesterService;
	}

	@PostMapping("/api/semester")
	public String addSemester(@RequestBody SemesterModel semesterModel) {
		try{
			return semesterService.save(semesterModel);
		}catch (Exception e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
			return null;
		}
	}
}
