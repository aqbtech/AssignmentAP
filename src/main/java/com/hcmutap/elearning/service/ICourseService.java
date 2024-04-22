package com.hcmutap.elearning.service;

import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.exception.TransactionalException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICourseService extends IGenericAdminService<CourseModel> {
    void delete(String id) throws TransactionalException;
    CourseModel getCourseInfo(String courseId) throws NotFoundException;
    List<ClassModel> getLichTrinh(String courseId) throws NotFoundException;
    List<PointModel> getListPointOfStudent(String courseId) throws NotFoundException;
    boolean isExist(String id);
    Page<CourseModel> getPage(String key, int page, int size);
}
