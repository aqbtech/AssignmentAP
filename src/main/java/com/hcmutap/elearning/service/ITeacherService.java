package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.TeacherModel;

public interface ITeacherService extends IGenericAdminService<TeacherModel> {
	boolean isExist(String username);
}
