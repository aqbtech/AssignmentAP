package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.PointModel;

import java.util.List;

public interface IPointService extends IGenericAdminService<PointModel> {
    PointModel getPoint(String studentID, String courseID);
    double getAveragePoint(String studentID, String courseID);
    List<PointModel> getListPointOfStudent(String studentID);
    List<PointModel> getListClassOfStudent(String studentID);
    List<PointModel> getListStudentOfClass(String classID);
    List<PointModel> getListStudentOfCourse(String courseID);
}
