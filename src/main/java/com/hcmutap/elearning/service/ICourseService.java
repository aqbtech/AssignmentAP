package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;

import java.util.List;

public interface ICourseService extends IGenericAdminService<CourseModel> {
    List<CourseModel> findAll();

    CourseModel findById(String courseId);

    String save(CourseModel courseModel);

    void update(CourseModel courseModel);

    void delete(String id);

    CourseModel getCourseInfo(String courseId);

    List<ClassModel> getLichTrinh(String courseId);

    List<PointModel> getListPointOfStudent(String courseId);
    boolean isExist(String id);
}
