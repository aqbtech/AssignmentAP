package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.impl.ClassDAO;
import com.hcmutap.elearning.dao.impl.PointDAO;
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
    private IStudentService studentService;
    @Resource
    private StudentDAO studentDAO;
    @Resource
    private PointDAO pointDAO;

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
        List<String> listClass = studentDAO.findBy("studentId", studentId).getFirst().getClasses();
        for (String item : listClass) {
            if (item.equals(classId)) {
                return false;
            }
        }
        if(!studentService.add_class_to_student(studentId, classId)){
            return false;
        }

        long timestamp = System.currentTimeMillis();
        String id = String.valueOf(timestamp);
        StudentModel studentModel = studentDAO.findById(studentId);
        ClassModel classModel = classDAO.getClassInfo(classId);
        // state = true is learned
        PointModel tmp = new PointModel(id, studentId, studentModel.getFullName(), classModel.getCourseId(),classModel.getCourseName(), classId,classModel.getClassName(), false, -1, -1, -1, -1);
        pointService.save(tmp);
        return true;
    }

    @Override
    public boolean NhapDiem(String studentId, String classId, PointDTO point) {
        // TODO: update diem cua 1 sinh vien
        // điểm phải được update trong bảng điểm của lớp -> gọi class service
        List<PointModel> listPoint = pointDAO.findBy("studentId", studentId);
        for (PointModel item : listPoint) {
            if (item.getClassId().equals(classId)) {
                // TODO: sua lai ham nay
                PointModel pointUpdate = new PointModel(item.getId(), item.getStudentId(), item.getStudentName(),
                        item.getCourseId(), item.getCourseName(), item.getClassId(), item.getClassName(),
                        item.isState(),point.getPointBT(), point.getPointBTL(), point.getPointGK(),point.getPointCK());
                pointService.update(pointUpdate);
                return true;
            }  // TODO : them truong hop khong tim thay
        }
        return false;
    }

    @Override
    public void NhapDiemCaLop(String classId, List<PointDTO> listPoint) {
        // TODO: update diem cua 1 lop
        List<PointModel> listPointClass = pointDAO.findBy("classId", classId);
        for(PointModel item: listPointClass){
            for(PointDTO point: listPoint){
                if(item.getStudentId().equals(point.getStudentId())){
                    NhapDiem(item.getStudentId(), classId, point);
                    break;
                }
            }
        }
    }
}