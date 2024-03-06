package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.TeacherModel;

import java.util.List;

public interface ITeacherService {
	List<TeacherModel> findAll();
	String save(TeacherModel teacherModel);
	void update(TeacherModel teacherModel);
	void delete(String id);
}
