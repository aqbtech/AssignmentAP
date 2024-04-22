package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.exception.TransactionalException;
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
    public ClassModel getClassInfo(String classId) throws TransactionalException {
        List<ClassModel> classModels =  findBy("classId", classId, Options.OptionBuilder.Builder().setEqual().build());
        if (classModels.isEmpty()) {
            throw new TransactionalException("Error when receive in database, class with id: " + classId);
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
        } catch (TransactionalException transactionalException) {
            throw new RuntimeException(transactionalException);
        }
        for(String classId : listClassId) {
            ClassModel classModel = null;
            try {
                classModel = findById(classId);
            } catch (TransactionalException transactionalException) {
                throw new RuntimeException(transactionalException);
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