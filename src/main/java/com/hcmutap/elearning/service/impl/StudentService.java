package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.StudentDAO;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.service.IStudentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements IStudentService {
	@Resource
	private StudentDAO studentDAO;

	@Override
	public List<StudentModel> findAll() {
		return studentDAO.findAll();
	}

	@Override
	public List<StudentModel> findBy(String key, String value) {
		return studentDAO.findBy(key, value);
	}

	@Override
	public StudentModel findById(String id) {
		return studentDAO.findById(id);
	}

	@Override
	public String save(StudentModel studentModel) {
		return studentDAO.save(studentModel);
	}

	@Override
	public void update(StudentModel studentModel) {
		studentDAO.update(studentModel);
	}
	@Override
	public StudentModel findByUsername(String username) {
		return studentDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
	}

	@Override
	public void delete(List<String> ids) {
		for (String id : ids) {
			studentDAO.delete(id);
		}
	}

	@Override
	public boolean isExist(String id) {
		return studentDAO.findById(id) != null;
	}
}
