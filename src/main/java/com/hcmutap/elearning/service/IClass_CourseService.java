package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.Class_CourseDTO;
import com.hcmutap.elearning.exception.MappingException;
import com.hcmutap.elearning.exception.NotFoundException;

import java.util.List;

public interface IClass_CourseService {
    List<Class_CourseDTO> getClass_Course(String studentId) throws NotFoundException, MappingException;
    List<Class_CourseDTO> getByCourseId(String courseId);
    boolean checkClass_Course(List<Class_CourseDTO> e);
}
