package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.TeacherDAO;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.repository.TeacherRepository;
import com.hcmutap.elearning.service.ITeacherService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService implements ITeacherService {
	@Autowired
	private TeacherRepository teacherRepository;
	@Resource
	private TeacherDAO teacherDAO;

	@Override
	public List<TeacherModel> findAll() {
		return teacherDAO.findAll();
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
	public void delete(String id) {
		teacherDAO.delete(id);
	}
}

