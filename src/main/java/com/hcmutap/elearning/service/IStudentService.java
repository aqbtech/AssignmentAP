package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.StudentModel;

import java.util.List;

public interface IStudentService {
	List<StudentModel> findAll();
	String save(StudentModel studentModel);
	void update(StudentModel studentModel);
	void delete(String id);
	StudentModel findById(String id);
//	String getScore(StudentModel studentModel);
//	void search_AccumulatedCredits(StudentModel studentModel);

}
