package com.hcmutap.elearning.service.impl;


import com.hcmutap.elearning.dao.impl.CourseDAO;
import com.hcmutap.elearning.dao.impl.PointDAO;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.IPointService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PointService implements IPointService {
    @Resource
    private PointDAO pointDAO;
    @Resource
    private CourseDAO courseDAO;

    @Override
    public List<PointModel> findAll() {
        return pointDAO.findAll();
    }
    @Override
    public PointModel findById(String id) { return pointDAO.findById(id); }

    @Override
    public void save(PointModel pointModel) {
        pointDAO.save(pointModel);
    }

    @Override
    public void update(PointModel pointModel) {
        pointDAO.update(pointModel);
    }

    @Override
    public void delete(String id) {
        pointDAO.delete(id);
    }

    @Override
    public  PointModel getPoint(String studentId, String courseId){
        List<PointModel> pointModelList = pointDAO.findAll();
        for(PointModel pointModel : pointModelList) {
            if (Objects.equals(pointModel.getStudentId(), studentId) && Objects.equals(pointModel.getCourseId(), courseId))
                return pointModel;
        }
        return null;
    }

    @Override
    public  double getAveragePoint(String studentId, String courseId){
        PointModel pointModel = getPoint(studentId,courseId);
        CourseModel courseModel = courseDAO.findById(pointModel.getCourseId());
        double averagePoint = pointModel.getPointBT()*courseModel.getPercentBT()
                            + pointModel.getPointBTL()*courseModel.getPercentBTL()
                            + pointModel.getPointGK()*courseModel.getPercentGK()
                            + pointModel.getPointCK()*courseModel.getPercentCK();
        return  averagePoint;
    }

    @Override
    public  List<PointModel> getListPointOfStudent(String studentId){
        List<PointModel> pointModelList = pointDAO.findAll();
        List<PointModel> pointModelList1= new ArrayList<>();
        for(PointModel pointModel : pointModelList) {
            if (Objects.equals(pointModel.getStudentId(), studentId))
                pointModelList1.add(pointModel);
        }
        return  pointModelList1;
    }

    @Override
    public List<PointModel> getListClassOfStudent(String studentId){
        List<PointModel> pointModelList = pointDAO.findAll();
        List<PointModel> pointModelList1= new ArrayList<>();
        for(PointModel pointModel : pointModelList) {
            if (Objects.equals(pointModel.getStudentId(), studentId))
                pointModelList1.add(pointModel);
        }
        return  pointModelList1;
    }

    @Override
    public List<PointModel> getListStudentOfClass(String classId){
        List<PointModel> pointModelList = pointDAO.findAll();
        List<PointModel> pointModelList1= new ArrayList<>();
        for(PointModel pointModel : pointModelList) {
            if (Objects.equals(pointModel.getClassId(), classId))
                pointModelList1.add(pointModel);
        }
        return  pointModelList1;
    }

    @Override
    public List<PointModel> getListStudentOfCourse(String courseId){
        List<PointModel> pointModelList = pointDAO.findAll();
        List<PointModel> pointModelList1= new ArrayList<>();
        for(PointModel pointModel : pointModelList) {
            if (Objects.equals(pointModel.getCourseId(), courseId))
                pointModelList1.add(pointModel);
        }
        return  pointModelList1;
    }

}
