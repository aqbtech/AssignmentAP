package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.impl.ClassDAO;
import com.hcmutap.elearning.dao.impl.StudentDAO;
import com.hcmutap.elearning.dto.PointDTO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.service.IClassService;
import com.hcmutap.elearning.service.IPointService;
import com.hcmutap.elearning.service.IStudentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService implements IClassService {
    @Resource
    private ClassDAO classDAO;
    @Resource
    private IPointService pointService;
    @Resource
    private StudentDAO studentDAO;

    @Override
    public List<ClassModel> findAll() {
        return classDAO.findAll();
    }

    @Override
    public List<ClassModel> findBy(String key, String value) {
        return null;
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
    public void delete(List<String> ids) {

    }

    @Override
    public Object findByUsername(String username) {
        return null;
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
    public List<PointModel> getListStudentOfClass(String classId) {
        return pointService.getListStudentOfClass(classId);
    }
    @Override
    public List<ClassModel> getTimeTableSV(String studentId){
        return classDAO.getTimeTableSV(studentId);
    }
    @Override
    public List<ClassModel> getTimeTableGV(String teacherId){
        return classDAO.getTimeTableGV(teacherId);
    }

    @Override
    public boolean addStudentToClass(String studentId, String classId) {
        List<PointModel> checks = pointService.findBy("studentId", studentId);
        for (PointModel point : checks) {
            if (point.getClassId().equals(classId)) {
                return false;
            }
        }
        StudentModel studentModel = studentDAO.findById(studentId);
        ClassModel classModel = classDAO.getClassInfo(classId);
        // state = true is learned
        PointModel tmp = new PointModel(null, studentId, studentModel.getName(), classModel.getCourseId(), classId, false, 0, 0, 0, 0);
        pointService.save(tmp);
        return true;
    }


    @Override
    public void NhapDiem(String studentId, String classId, PointDTO point) {
        // TODO: update diem cua 1 sinh vien
        // điểm phải được update trong bảng điểm của lớp -> gọi class service

    }

    @Override
    public void NhapDiemCaLop(String classId, List<PointDTO> listPoint) {
        // TODO: update diem cua 1 lop
    }
}