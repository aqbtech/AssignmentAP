package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.exception.NotFoundInDB;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.service.IPointService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClassDAO extends DefaultFirebaseDatabase<ClassModel, String> {

	@Autowired
	private StudentDAO studentDAO;
    public ClassModel getClassInfo(String classId) throws NotFoundInDB {
        List<ClassModel> classModels =  findBy("classId", classId, Options.OptionBuilder.Builder().setEqual().build());
        if (classModels.isEmpty()) {
            throw new NotFoundInDB("Error when receive in database, class with id: " + classId);
        }
        return classModels.getFirst();
    }

    public List<ClassModel> getClassOfCourse(String courseId) {
        return findBy("courseId", courseId, Options.OptionBuilder.Builder().setEqual().build());
    }


    @Resource
    private IPointService pointService;
    public List<ClassModel> getTimeTableSV(String studentId) {
        List<ClassModel> TimeTableList = new ArrayList<>();
        List<String> listClassId = null;
        try {
            listClassId = studentDAO.findById(studentId).getClasses();
        } catch (NotFoundInDB notFoundInDB) {
            throw new RuntimeException(notFoundInDB);
        }
        for(String classId : listClassId) {
            ClassModel classModel = null;
            try {
                classModel = findById(classId);
            } catch (NotFoundInDB notFoundInDB) {
                throw new RuntimeException(notFoundInDB);
            }
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