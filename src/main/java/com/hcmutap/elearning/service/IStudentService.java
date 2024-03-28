package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.PointModel;

import java.util.List;

public interface IStudentService {
	List<StudentModel> findAll();
	String save(StudentModel studentModel);
	void update(StudentModel studentModel);
	void delete(String id);
	StudentModel findById(String id); // Student information by studentID
	void DangkiMonhoc(StudentModel studentModel, String courseID);
	List<ClassModel> get_timetable(String studentId);
	List<CourseModel> Tientrinhhoctap(String studentId);
	List<PointModel> get_point(String studentId);
	
//	String getScore(StudentModel studentModel);
//	void search_AccumulatedCredits(StudentModel studentModel);

}
