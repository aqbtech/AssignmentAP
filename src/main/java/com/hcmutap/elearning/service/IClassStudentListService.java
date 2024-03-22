package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.ClassStudentListModel;

import java.util.List;

public interface IClassStudentListService {
    List<ClassStudentListModel> findAll();
    void save(ClassStudentListModel classStudentListModel);
    void update(ClassStudentListModel classStudentListModel);
    void delete(String id);
    List<ClassStudentListModel> findStudentByClassId(String classId);


}
