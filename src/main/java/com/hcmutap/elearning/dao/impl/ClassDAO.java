package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.AdminDAO;
import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.exception.NotFoundInDB;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.IPointService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClassDAO extends DefaultFirebaseDatabase<ClassModel, String> implements AdminDAO<ClassModel> {

	@Autowired
	private StudentDAO studentDAO;

    public ClassModel findById(String id) throws NotFoundException {
        List<ClassModel> classModels =  findBy("classId", id, Options.OptionBuilder.Builder().setEqual().build());
        if (classModels.isEmpty()) {
            throw new NotFoundException("Not found class with id: " + id);
        }
        return classModels.getFirst();
    }

    public ClassModel getClassInfo(String classId) throws NotFoundException {
        List<ClassModel> classModels =  findBy("classId", classId, Options.OptionBuilder.Builder().setEqual().build());
        if (classModels.isEmpty()) {
            throw new NotFoundException("Not found class with id: " + classId);
        }
        return classModels.getFirst();
    }

    public List<ClassModel> getClassOfCourse(String courseId) {
        return findBy("courseId", courseId, Options.OptionBuilder.Builder().setEqual().build());
    }


    @Resource
    private IPointService pointService;
    public List<ClassModel> getTimeTableSV(String studentId) throws NotFoundException {
        List<ClassModel> TimeTableList = new ArrayList<>();
        List<String> listClassId = studentDAO.findById(studentId).getClasses();
        for(String classId : listClassId) {
            ClassModel classModel = findById(classId);
            if(classModel != null) {
                TimeTableList.add(classModel);
            }
        }
        return TimeTableList;
    }


    public List<ClassModel> getTimeTableGV(String teacherId) {
        return findBy("teacherId", teacherId, Options.OptionBuilder.Builder().setEqual().build());
    }
}