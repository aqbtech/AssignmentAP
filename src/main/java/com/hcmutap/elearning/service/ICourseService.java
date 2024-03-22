package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.CourseModel;


import java.util.List;
public interface ICourseService {
    List<CourseModel> findAll();
    void save(CourseModel courseModel);
    void update(CourseModel courseModel);
    void delete(String id);

}
