package com.hcmutap.elearning.controller.api;

import com.hcmutap.elearning.model.SemesterModel;
import com.hcmutap.elearning.service.impl.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SemesterAPI {
	private final SemesterService semesterService;
	@Autowired
	public SemesterAPI(SemesterService semesterService) {
		this.semesterService = semesterService;
	}

	@PostMapping("/api/semester")
	public String addSemester(@RequestBody SemesterModel semesterModel) {
		return semesterService.save(semesterModel);
	}
}
