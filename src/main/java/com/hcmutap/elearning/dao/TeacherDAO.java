package com.hcmutap.elearning.dao;

import com.hcmutap.elearning.model.TeacherModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class TeacherDAO extends AbstractDAO<TeacherModel> {
	public List<TeacherModel> findAll() {
		List<TeacherModel> teacherModelList = null;
		try {
			teacherModelList = query("teachers", TeacherModel.class);
		}
		catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		return teacherModelList;
	}
	public String save(TeacherModel teacherModel) {
		return create("teachers", teacherModel);
	}
	public TeacherModel update(TeacherModel teacherModel) {
		List<String> ids = null;
		try {
			ids = findDocument("teachers", "id", teacherModel.getId());
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		assert ids != null; // handle not found exception
		return update("teachers", ids.getFirst(), teacherModel);
	}
	public void delete(String id) {
		List<String> ids = null;
		try {
			ids = findDocument("teachers", "id", id);
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		assert ids != null; // handle not found exception
		delete("teachers", ids.getFirst());
	}
	public TeacherModel findById(String id) {
		TeacherModel teacherModel = null;
		try {
			teacherModel = queryBy("teachers","id", id, TeacherModel.class).getFirst();
		}
		catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		return teacherModel;
	}
}
