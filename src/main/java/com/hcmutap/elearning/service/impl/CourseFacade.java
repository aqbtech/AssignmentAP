package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.service.ICourseFacade;
import lombok.Getter;

public class CourseFacade implements ICourseFacade {
	@Getter
	private static final CourseFacade INSTANCE = new CourseFacade();
	private CourseService courseService;
	private ClassService classService;
	private PointService pointService;
	private CourseFacade() {
		courseService = new CourseService();
		classService = new ClassService();
		pointService = new PointService();
	}
	@Override
	public boolean addStudentToClass(String studentId, String classId) {
		return classService.addStudentToClass(studentId, classId);
	}
}
