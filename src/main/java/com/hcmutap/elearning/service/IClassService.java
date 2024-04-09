package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.PointDTO;
import com.hcmutap.elearning.model.*;

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
    InfoClassModel getClassDocs(String classId);
    boolean updateTileOfDoc(String classId, Document docCurrent, String newTitle);
    boolean addFileOfDoc(String classId, Document docCurrent, FileInfo file);
    boolean deleteFileOfDoc(String classId, Document docCurrent, FileInfo file);
    boolean addNewDoc(String classId);
    boolean addDoc(String classId, Document doc);
    boolean deleteDoc(String classId, Document doc);
}