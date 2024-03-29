package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.PointModel;

import java.util.List;

public interface IClassService {
    List<ClassModel> findAll();
    ClassModel findById(String id);
    String save(ClassModel classModel);
    void update(ClassModel classModel);
    void delete(String id);
    ClassModel getClassInfo(String classId);
    List<ClassModel> getClassOfCourse(String courseId);
    List<PointModel> getListStudentOfClass(String classId);
    List<ClassModel> getTimeTableSV(String studentId);
    List<ClassModel> getTimeTableGV(String teacherId);
}