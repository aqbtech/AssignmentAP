package com.hcmutap.elearning.service.impl;


import com.hcmutap.elearning.dao.CourseDAO;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.IClassService;
import com.hcmutap.elearning.service.ICourseService;
import com.hcmutap.elearning.service.IPointService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService implements ICourseService {
    @Resource
    private CourseDAO courseDAO;
    @Resource
    private IClassService classService;
    @Resource
    private IPointService pointService;

    @Override
    public List<CourseModel> findAll() {
        return courseDAO.findAll();
    }

    @Override
    public CourseModel findById(String courseId) {
        return courseDAO.findById(courseId);
    }

    @Override
    public String save(CourseModel courseModel) {
        return courseDAO.save(courseModel);
    }

    @Override
    public void update(CourseModel courseModel) {
        courseDAO.update(courseModel);
    }

    @Override
    public void delete(String courseId) {
        courseDAO.delete(courseId);
    }

    @Override
    public CourseModel getCourseInfo(String courseId) {
        return this.findById(courseId);
    }

    @Override
    public List<ClassModel> getLichTrinh(String courseId) {
        return classService.getClassOfCourse(courseId);
    }

    @Override
    public List<PointModel> getListPointOfStudent(String courseId) {
        return pointService.getListStudentOfCourse(courseId);
    }

}
