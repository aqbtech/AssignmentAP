package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.PointModel;

import java.util.List;

public interface IStudentService extends IGenericAdminService<StudentModel>{
	List<StudentModel> findAll();
	String save(StudentModel studentModel);
	void update(StudentModel studentModel);
	void delete(List<String> ids);
	StudentModel findById(String id); // Student information by studentID
	String DangkiMonhoc(String studentId, String classID);
	StudentModel findByUsername(String username);
	List<ClassModel> get_timetable(String studentId);
	List<CourseModel> get_course(String studentId);
	List<PointModel> Tientrinhhoctap(String studentId);
	List<PointModel> get_point(String studentId);
	List<ClassModel> get_list_class_of_this_course(String courseId);
	String add_class_to_student(String studentId, String classId);
	List<ClassModel> getAllClass(String username);
}