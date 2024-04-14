package com.hcmutap.elearning.service;

import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.TeacherModel;

import java.util.List;

public interface ITeacherService extends IGenericAdminService<TeacherModel> {
	TeacherModel findByUsername(String username) throws NotFoundException;
	boolean isExist(String username);
	List<ClassModel> getAllClass(String name) throws NotFoundException;
	String Dangkilophoc(String teacherId, String classId) throws NotFoundException;
	List<CourseModel> get_course(String teacherId);
	boolean isExistTeacherInClass(String username, String classId) throws NotFoundException;

//	List<CourseModel> getCoursesModel(String teacherId, List<ClassModel> classes);
}
