package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.TeacherModel;

import java.util.List;

public interface ITeacherService extends IGenericAdminService<TeacherModel> {
	boolean isExist(String username);

	List<ClassModel> getAllClass(String username);
}
