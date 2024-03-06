package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.repository.StudentRepository;
import com.hcmutap.elearning.repository.document.StudentDocument;
import com.hcmutap.elearning.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService implements IStudentService {
	@Autowired
	private StudentRepository studentRepository;
	@Override
	public List<StudentModel> findAll() {
		List<StudentModel> studentModelList = new ArrayList<>();
		List<StudentDocument> studentDocumentList = studentRepository.findAll();
		for(StudentDocument studentDocument:studentDocumentList){
			StudentModel studentModel = new StudentModel();
			studentModel.setId(studentDocument.getId());
			studentModel.setName(studentDocument.getName());
			studentDocument.setStudentId(studentModel.getStudentId());
			studentModelList.add(studentModel);
		}
		return studentModelList;
	}

	@Override
	public void save(StudentModel studentModel) {
		StudentDocument studentDocument = new StudentDocument();
		studentDocument.setId(studentModel.getId());
		studentDocument.setName(studentModel.getName());
		studentDocument.setStudentId(studentModel.getStudentId());
		studentRepository.push(studentDocument);
	}

	@Override
	public void update(StudentModel studentModel) {
		StudentDocument studentDocument = new StudentDocument();
		studentDocument.setId(studentModel.getId());
		studentDocument.setName(studentModel.getName());
		studentDocument.setStudentId(studentModel.getStudentId());
		studentRepository.update(studentDocument);
	}

	@Override
	public void delete(String id) {
		studentRepository.remove(id);
	}
}
