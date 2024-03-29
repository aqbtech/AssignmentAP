package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.StudentDAO;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.repository.StudentRepository;
import com.hcmutap.elearning.repository.document.StudentDocument;
import com.hcmutap.elearning.service.IStudentService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService implements IStudentService {
//	@Autowired
//	private StudentRepository studentRepository;
	@Resource
	private StudentDAO studentDAO;
	@Override
	public List<StudentModel> findAll() {
		return studentDAO.findAll();
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
	public void delete(String id) {
		studentDAO.delete(id);
	}
	@Override
	public StudentModel findById(String id){
		return studentDAO.findById(id);
	}
//	@Override
//	public String getScore(StudentModel studentModel) {
//
//	}
//	@Override
//	public void search_AccumulatedCredits(StudentModel studentModel){
//
//	}
}
