package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.Class_CourseDTO;
import com.hcmutap.elearning.exception.NotFoundException;

import java.util.List;

public interface IClass_CourseService {
    List<Class_CourseDTO> getClass_Course(String studentId) throws NotFoundException;
    List<Class_CourseDTO> getByCourseId(String courseId);
}
