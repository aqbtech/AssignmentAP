package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.ClassModel;

public interface ICourseFacade {
	boolean addStudentToClass(String studentId, String classId);
	public ClassModel findClassById(String classId);
}
