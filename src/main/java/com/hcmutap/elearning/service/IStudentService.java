package com.hcmutap.elearning.service;

import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.PointModel;

import java.util.List;

public interface IStudentService extends IGenericAdminService<StudentModel>{
	StudentModel findByUsername(String username) throws NotFoundException;
	String DangkiMonhoc(String studentId, String classID) throws NotFoundException;
	List<ClassModel> getTimetableById(String studentId) throws NotFoundException;
	List<CourseModel> getCourseByIf(String studentId) throws NotFoundException;
	List<PointModel> LearningProcess(String studentId) throws NotFoundException;
	List<PointModel> getPointById(String studentId) throws NotFoundException;
	List<ClassModel> getListClassByCourseId(String courseId);
	String add_class_to_student(String studentId, String classId) throws NotFoundException;
	List<ClassModel> getAllClass(String username) throws NotFoundException;
	boolean isExist(String id);
}