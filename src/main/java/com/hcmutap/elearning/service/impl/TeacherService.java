package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.AdminDAO;
import com.hcmutap.elearning.dao.impl.TeacherDAO;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.ITeacherService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("teacherService")
public class TeacherService implements ITeacherService {
	@Resource
	private TeacherDAO teacherDAO;
	@Resource
	private AdminDAO<TeacherModel> basicDAO;
	@Override
	public List<TeacherModel> findAll() {
//		basicDAO.findAll();
		return teacherDAO.findAll();
	}

	@Override
	public List<TeacherModel> findBy(String key, String value) {
		return null;
	}

	@Override
	public TeacherModel findById(String id) {
		return teacherDAO.findById(id);
	}

	@Override
	public String save(TeacherModel teacherModel) {
		return teacherDAO.save(teacherModel);
	}

	@Override
	public void update(TeacherModel teacherModel) {
		teacherDAO.update(teacherModel);
	}

	@Override
	public void delete(List<String> ids) {
		for (String id : ids) {
			teacherDAO.delete(id);
		}
	}
}

