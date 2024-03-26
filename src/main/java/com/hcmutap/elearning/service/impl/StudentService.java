package com.hcmutap.elearning.service.impl;

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
	public void save(StudentModel studentModel) {
		studentDAO.save(studentModel);
	}

	@Override
	public void update(StudentModel studentModel) {
		studentDAO.update(studentModel);
	}

	@Override
	public void delete(String id) {
		studentDAO.delete(id);
	}
}
