package com.hcmutap.elearning.dao;

import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.IPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class ClassDAO extends AbstractDAO<ClassModel> {
    public List<ClassModel> findAll() {
        List<ClassModel> classModelList = null;
        try {
            classModelList = query("classes", ClassModel.class);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return classModelList;
    }

    public String save(ClassModel classModel) {
        return create("classes", classModel);
    }

    public ClassModel update(ClassModel classModel) {
        List<String> ids = null;
        try {
            ids = findDocument("classes", "id", classModel.getId());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        assert ids != null; // handle not found exception
        return update("classes", ids.getFirst(), classModel);
    }

    public void delete(String id) {
        List<String> ids = null;
        try {
            ids = findDocument("classes", "id", id);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        assert ids != null; // handle not found exception
        delete("classes", ids.getFirst());
    }

    public ClassModel findById(String id) {
        ClassModel classModel = null;
        try {
            classModel = queryBy("classes", "id", id, ClassModel.class).getFirst();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return classModel;
    }

    public ClassModel getClassInfo(String classId) {
        ClassModel classModel = null;
        try {
            classModel = queryBy("classes", "classId", classId, ClassModel.class).getFirst();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return classModel;
    }

    public List<ClassModel> getClassOfCourse(String courseId) {
        List<ClassModel> classOfCourseList = null;
        try {
            classOfCourseList = queryBy("classes", "courseId", courseId, ClassModel.class);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return classOfCourseList;
    }


    @Autowired
    private IPointService pointService;
    public List<ClassModel> getTimeTableSV(String studentId) {
        List<ClassModel> TimeTableList = new ArrayList<>();
        try {
            List<PointModel> listclass = pointService.getListPointOfStudent(studentId);
            for (PointModel course : listclass) {
                String classId = course.getClassId();
                ClassModel tmp = queryBy("classes", "classId", classId, ClassModel.class).getFirst();
                TimeTableList.add(tmp);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return TimeTableList;
    }


    public List<ClassModel> getTimeTableGV(String teacherId) {
        List<ClassModel> TimeTableList = null;
        try{
            TimeTableList = queryBy("classes", "teacherId", teacherId, ClassModel.class);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return TimeTableList;
    }
}