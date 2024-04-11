package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.TeacherDAO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("teacherService")
public class TeacherService implements ITeacherService {
	private final TeacherDAO teacherDAO;
	@Autowired
	public TeacherService(TeacherDAO teacherDAO) {
		this.teacherDAO = teacherDAO;
	}
	@Override
	public List<TeacherModel> findAll() {
		return teacherDAO.findAll();
	}
	@Override
	public List<TeacherModel> findBy(String key, String value) throws NotFoundException {
		List<TeacherModel> teacherModels = teacherDAO.findBy(key, value, Options.OptionBuilder.Builder().setEqual().build());
		if (teacherModels != null && !teacherModels.isEmpty()){
			return teacherModels;
		} else {
			throw new NotFoundException("Teacher not found");
		}
	}

	@Override
	public TeacherModel findById(String id) throws NotFoundException {
		TeacherModel teacherModel = teacherDAO.findById(id);
		if (teacherModel != null){
			return teacherModel;
		} else {
			throw new NotFoundException("Teacher not found");
		}
	}

	@Override
	public String save(TeacherModel teacherModel) {
		return teacherDAO.save(teacherModel);
	}

	@Override
	public void update(TeacherModel teacherModel) throws NotFoundException {
		TeacherModel teacherModels = teacherDAO.findById(teacherModel.getId());
		if (teacherModels == null){
			throw new NotFoundException("Teacher not found");
		}
		teacherDAO.update(teacherModel);
	}
	private void delete(String id) throws NotFoundException {
		TeacherModel teacherModel = teacherDAO.findById(id);
		if (teacherModel == null){
			throw new NotFoundException("Teacher not found id: " + id);
		}
		teacherDAO.delete(id);
	}
	@Override
	public void delete(List<String> ids) throws NotFoundException {
		for(String id : ids){
			delete(id);
		}
	}

	@Override
	public TeacherModel findByUsername(String username) throws NotFoundException {
		List<TeacherModel> teacherModels = teacherDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build());
		if (teacherModels != null && !teacherModels.isEmpty()){
			return teacherModels.getFirst();
		} else {
			throw new NotFoundException("Teacher with username " +username + "not found");
		}
	}

	@Override
	public boolean isExist(String id) {
		TeacherModel teacherModel =  teacherDAO.findById(id);
		return teacherModel != null;
	}

	@Override
	public List<ClassModel> getAllClass(String username) throws NotFoundException {
		List<ClassModel> classModels = new ArrayList<>();
		try {
			TeacherModel teacherModel = teacherDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
			if (teacherModel.getClasses() == null || teacherModel.getClasses().isEmpty()){
				throw new NotFoundException("Teacher not found");
			} else {
				for(String classId : teacherModel.getClasses()){
					ClassModel classModel = CourseFacade.getInstance().findClassById(classId);
					classModels.add(classModel);
				}
			}
			return classModels;
		} catch (Exception e){
			throw new NotFoundException("Teacher not found");
		}
	}
}

