package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.AdminDAO;
import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.SemesterModel;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class SemesterDAO extends DefaultFirebaseDatabase<SemesterModel,String> implements AdminDAO<SemesterModel> {

	@Override
	public SemesterModel findById(String id) throws NotFoundException {
		List<SemesterModel> semesterModels = findBy("id", id, Options.OptionBuilder.Builder().setEqual().build());
		if ( semesterModels.isEmpty() ) {
			throw new NotFoundException("Semester not found");
		} else {
			return semesterModels.getFirst();
		}
	}
}
