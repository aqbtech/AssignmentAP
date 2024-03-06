package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.StudentModel;

import java.util.List;

public interface IStudentService {
	List<StudentModel> findAll();
	void save(StudentModel studentModel);
	void update(StudentModel studentModel);
	void delete(String id);
}
