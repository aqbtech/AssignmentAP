package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.Class_CourseDTO;

import java.util.List;

public interface IClass_CourseService {
    List<Class_CourseDTO> getClass_Course(String studentId);
    List<Class_CourseDTO> getByCourseId(String courseId);
}
