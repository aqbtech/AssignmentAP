package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.AdminDAO;
import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.IPointService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClassDAO extends DefaultFirebaseDatabase<ClassModel, String> implements AdminDAO<ClassModel> {

    public ClassModel findById(String id) {
        return findBy("classId", id, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
    }

    public ClassModel getClassInfo(String classId) {
        return findBy("classId", classId, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
    }

    public List<ClassModel> getClassOfCourse(String courseId) {
        return findBy("courseId", courseId, Options.OptionBuilder.Builder().setEqual().build());
    }


    @Resource
    private IPointService pointService;
    public List<ClassModel> getTimeTableSV(String studentId) throws NotFoundException {
        List<ClassModel> TimeTableList = new ArrayList<>();
        List<PointModel> listClass = pointService.getListPointByStudentId(studentId);
        for (PointModel course : listClass) {
            String classId = course.getClassId();
            ClassModel tmp = findById(classId);
            TimeTableList.add(tmp);
        }
        return TimeTableList;
    }


    public List<ClassModel> getTimeTableGV(String teacherId) {
        return findBy("teacherId", teacherId, Options.OptionBuilder.Builder().setEqual().build());
    }
}