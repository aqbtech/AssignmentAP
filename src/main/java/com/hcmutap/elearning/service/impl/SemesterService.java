package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.SemesterDAO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.SemesterModel;
import com.hcmutap.elearning.service.ISemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SemesterService implements ISemesterService {
	private final SemesterDAO semesterDAO;
	@Autowired
	public SemesterService(SemesterDAO semesterDAO) {
		this.semesterDAO = semesterDAO;
	}
	@Override
	public List<SemesterModel> findAll() {
		return semesterDAO.findAll();
	}

	@Override
	public List<SemesterModel> findBy(String key, String value) throws NotFoundException {
		List<SemesterModel> semesters = semesterDAO.findBy(key, value, Options.OptionBuilder.Builder().setEqual().build());
		if (semesters.isEmpty()) {
			throw new NotFoundException("Semester not found");
		} else {
			return semesters;
		}
	}

	@Override
	public SemesterModel findById(String id) throws NotFoundException {
		List<SemesterModel> semesters = semesterDAO.findBy("id", id, Options.OptionBuilder.Builder().setEqual().build());
		if (semesters.isEmpty()) {
			throw new NotFoundException("Semester not found");
		} else {
			return semesters.getFirst();
		}
	}

	@Override
	public String save(SemesterModel object) {
		return semesterDAO.save(object);
	}

	@Override
	public void update(SemesterModel object) throws NotFoundException {
		List<SemesterModel> semesters = semesterDAO.findBy("id", object.getId(), Options.OptionBuilder.Builder().setEqual().build());
		if (semesters.isEmpty()) {
			throw new NotFoundException("Semester not found");
		} else {
			semesterDAO.update(object);
		}
	}

	@Override
	public void delete(List<String> ids) throws NotFoundException {
		for (String id : ids) {
			List<SemesterModel> semesters = semesterDAO.findBy("id", id, Options.OptionBuilder.Builder().setEqual().build());
			if (semesters.isEmpty()) {
				throw new NotFoundException("Semester not found");
			} else {
				semesterDAO.delete(id);
			}
		}
	}

	@Override
    public SemesterModel getSemeter(String semesterId){
		List<SemesterModel> semesterModels = semesterDAO.findAll();
		for(SemesterModel semesterModel1 : semesterModels){
			if(Objects.equals(semesterId, semesterModel1.getSemesterName())){
				return  semesterModel1;
			}
		}
		return null;
	}

}
