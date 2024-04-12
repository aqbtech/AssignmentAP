package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.TeacherModel;

import java.util.List;

public interface ITeacherService extends IGenericAdminService<TeacherModel> {
	boolean isExist(String username);

	List<ClassModel> getAllClass(String name);
	TeacherModel findByUsername(String username);
	String Dangkilophoc(String teacherId, String classId);

	List<CourseModel> get_course(String teacherId);
//	List<CourseModel> getCoursesModel(String teacherId, List<ClassModel> classes);
}
