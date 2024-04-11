package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.PointDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.*;

import java.util.List;

public interface IClassService extends IGenericAdminService<ClassModel> {
    ClassModel getClassInfo(String classId) throws NotFoundException;
    List<ClassModel> getClassOfCourse(String courseId) throws NotFoundException;
    List<PointModel> getListStudentOfClass(String classId) throws NotFoundException;
    List<ClassModel> getTimeTableSV(String studentId) throws NotFoundException;
    List<ClassModel> getTimeTableGV(String teacherId) throws NotFoundException;
    boolean addStudentToClass(String studentId, String classId) throws NotFoundException;
    boolean NhapDiem(String studentId, String classId, PointDTO point) throws NotFoundException;
    void NhapDiemCaLop(String classId, List<PointDTO> listPoint) throws NotFoundException;
    InfoClassModel getClassDocs(String classId);
    boolean updateTileOfDoc(String classId, Document docCurrent, String newTitle);
    boolean addFileOfDoc(String classId, Document docCurrent, FileInfo file);
    boolean deleteFileOfDoc(String classId, Document docCurrent, FileInfo file);
    boolean addNewDoc(String classId);
    boolean addDoc(String classId, Document doc);
    boolean deleteDoc(String classId, Document doc);
}