package com.hcmutap.elearning.service;

import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;

import java.util.List;

public interface ICourseService extends IGenericAdminService<CourseModel> {
    void delete(String id);
    CourseModel getCourseInfo(String courseId);
    List<ClassModel> getLichTrinh(String courseId) throws NotFoundException;
    List<PointModel> getListPointOfStudent(String courseId) throws NotFoundException;
    boolean isExist(String id);
}
