package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.impl.ClassDAO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.IClassService;
import com.hcmutap.elearning.service.IPointService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService implements IClassService {
    @Resource
    private ClassDAO classDAO;
    @Resource
    private IPointService pointService;

    @Override
    public List<ClassModel> findAll() {
        return classDAO.findAll();
    }
    @Override
    public ClassModel findById(String id) {
        return classDAO.findById(id);
    }
    @Override
    public String save(ClassModel classModel) {
        return classDAO.save(classModel);
    }
    @Override
    public void update(ClassModel classModel) {
        classDAO.update(classModel);
    }
    @Override
    public void delete(String id) {
        classDAO.delete(id);
    }
    @Override
    public ClassModel getClassInfo(String classId) {
        return classDAO.getClassInfo(classId);
    }
    @Override
    public List<ClassModel> getClassOfCourse(String courseId) {
        return classDAO.getClassOfCourse(courseId);
    }
    @Override
    public List<PointModel> getListStudentOfClass(String classId) {return pointService.getListStudentOfClass(classId);}
    @Override
    public List<ClassModel> getTimeTableSV(String studentId){ return classDAO.getTimeTableSV(studentId);}
    @Override
    public List<ClassModel> getTimeTableGV(String teacherId){
        return classDAO.getTimeTableGV(teacherId);
    }
}