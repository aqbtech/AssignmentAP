package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.PointDTO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.PointModel;

import java.util.List;

public interface IClassService extends IGenericAdminService<ClassModel>{
    ClassModel getClassInfo(String classId);
    List<ClassModel> getClassOfCourse(String courseId);
    List<PointModel> getListStudentOfClass(String classId);
    List<ClassModel> getTimeTableSV(String studentId);
    List<ClassModel> getTimeTableGV(String teacherId);
    boolean addStudentToClass(String studentId, String classId);
    boolean NhapDiem(String studentId, String classId, PointDTO point);
    void NhapDiemCaLop(String classId, List<PointDTO> listPoint);

}