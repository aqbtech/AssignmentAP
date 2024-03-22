package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.ClassModel;

import java.util.List;

public interface IClassService {
    List<ClassModel> findAll();
    void save(ClassModel classModel);
    void update(ClassModel classModel);
    void delete(String id);

    List<ClassModel> getClassList (String courseId);
}
