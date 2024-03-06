package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.repository.TeacherRepository;
import com.hcmutap.elearning.repository.document.TeacherDocument;
import com.hcmutap.elearning.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherService implements ITeacherService {
	@Autowired
	private TeacherRepository teacherRepository;

	@Override
	public List<TeacherModel> findAll() {
		List<TeacherModel> teacherModelList = new ArrayList<>();
		teacherRepository.findAll().forEach(teacherDocument -> {
			TeacherModel teacherModel = new TeacherModel();
			teacherModel.setId(teacherDocument.getId());
			teacherModel.setName(teacherDocument.getName());
			teacherModelList.add(teacherModel);
		});
		return teacherModelList;
	}

	@Override
	public String save(TeacherModel teacherModel) {
		TeacherDocument teacherDocument = new TeacherDocument();
		teacherDocument.setId(teacherModel.getId());
		teacherDocument.setName(teacherModel.getName());
		teacherDocument.setTeacherId(teacherModel.getTeacherId());
		return teacherRepository.push(teacherDocument).getId();
	}

	@Override
	public void update(TeacherModel teacherModel) {
		TeacherDocument teacherDocument = new TeacherDocument();
		teacherDocument.setId(teacherModel.getId());
		teacherDocument.setName(teacherModel.getName());
		teacherDocument.setTeacherId(teacherModel.getTeacherId());
		teacherRepository.update(teacherDocument);
	}

	@Override
	public void delete(String id) {
		teacherRepository.remove(id);
	}
}

