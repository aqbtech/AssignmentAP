package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.IPointService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class ClassDAO extends DefaultFirebaseDatabase<ClassModel, String>{
//    public List<ClassModel> findAll() {
//        List<ClassModel> classModelList = null;
//        try {
//            classModelList = query("classes", ClassModel.class);
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return classModelList;
//    }
//
//    public String save(ClassModel classModel) {
//        return create("classes", classModel);
//    }
//
//    public ClassModel update(ClassModel classModel) {
//        List<String> ids = null;
//        try {
//            ids = findDocument("classes", "id", classModel.getId());
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        assert ids != null; // handle not found exception
//        return update("classes", ids.getFirst(), classModel);
//    }
//
//    public void delete(String id) {
//        List<String> ids = null;
//        try {
//            ids = findDocument("classes", "id", id);
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        assert ids != null; // handle not found exception
//        delete("classes", ids.getFirst());
//    }
//
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
    public List<ClassModel> getTimeTableSV(String studentId) {
        List<ClassModel> TimeTableList = new ArrayList<>();
        List<PointModel> listClass = pointService.getListPointOfStudent(studentId);
        for (PointModel course : listClass) {
            String classId = course.getClassId();
//            ClassModel tmp = queryBy("classes", "classId", classId, ClassModel.class).getFirst();
            ClassModel tmp = findById(classId);
            TimeTableList.add(tmp);
        }
        return TimeTableList;
    }


    public List<ClassModel> getTimeTableGV(String teacherId) {
        return findBy("teacherId", teacherId, Options.OptionBuilder.Builder().setEqual().build());
    }
}