package com.hcmutap.elearning.dao.impl;


import com.hcmutap.elearning.dao.ITeacherDAO;
import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.model.TeacherModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("teacherDAO")
public class TeacherDAO extends DefaultFirebaseDatabase<TeacherModel,String> implements ITeacherDAO {

	@Override
	public TeacherModel findById(String id) {
		List<TeacherModel> teacherModels = findBy("id", id, Options.OptionBuilder.Builder().setEqual().build());
		assert teacherModels != null : "Teacher not found";
		return teacherModels.isEmpty()? null : teacherModels.getFirst();
	}
}
