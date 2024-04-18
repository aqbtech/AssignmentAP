package com.hcmutap.elearning.service;

import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPointService extends IGenericAdminService<PointModel> {
    PointModel getPoint(String studentID, String courseID);
    double getAveragePoint(String studentID, String courseID);
    List<PointModel> getListPointByStudentId(String studentID) throws NotFoundException;
    List<PointModel> getListStudentByClassId(String classID) throws NotFoundException;
    List<PointModel> getListStudentByCourseId(String courseID) throws NotFoundException;
    Page<PointModel> getPage(String key, String id, int page, int size);
}
