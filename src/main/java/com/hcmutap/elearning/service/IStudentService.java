package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.StudentModel;

import java.util.List;

public interface IStudentService  extends IGenericAdminService<StudentModel>{
	boolean isExist(String id);

}
