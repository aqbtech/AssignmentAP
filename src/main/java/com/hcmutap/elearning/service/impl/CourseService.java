package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.impl.CourseDAO;
import com.hcmutap.elearning.exception.NotFoundException;
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
    public CourseService() {
        courseDAO = new CourseDAO();
    }
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
    public List<CourseModel> findBy(String key, String value) {
        return null;
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
    public void delete(List<String> ids) {

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
    public List<ClassModel> getLichTrinh(String courseId) throws NotFoundException {
        return classService.getClassOfCourse(courseId);
    }

    @Override
    public List<PointModel> getListPointOfStudent(String courseId) throws NotFoundException {
        return pointService.getListStudentByCourseId(courseId);
    }
}
