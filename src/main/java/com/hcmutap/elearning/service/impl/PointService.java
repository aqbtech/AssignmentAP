package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.CourseDAO;
import com.hcmutap.elearning.dao.impl.PointDAO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.IPointService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public List<PointModel> findBy(String key, String value) throws NotFoundException {
        List<PointModel> pointModelList = pointDAO.findBy(key, value, Options.OptionBuilder.Builder().setEqual().build());
        if (pointModelList.isEmpty()) {
            throw new NotFoundException("Not found point with " + key + " = " + value);
        }
        else {
            return pointModelList;
        }
    }

    @Override
    public PointModel findById(String id) throws NotFoundException {
        List<PointModel> pointModelList = pointDAO.findAll();
        if (pointModelList.isEmpty()) {
            throw new NotFoundException("Not found point with id: " + id);
        }
        else {
            return pointDAO.findById(id);
        }
	}

    @Override
    public String save(PointModel pointModel) {
        return pointDAO.save(pointModel);
    }

    @Override
    public void update(PointModel pointModel) throws NotFoundException {
        Optional<PointModel> optionalPointModel = Optional.ofNullable(pointDAO.findById(pointModel.getId()));
        if (optionalPointModel.isEmpty()) {
            throw new NotFoundException("Not found point with id: " + pointModel.getId());
        }
        pointDAO.update(pointModel);
    }

    @Override
    public void delete(List<String> ids) throws NotFoundException {
        for (String id : ids) {
            Optional<PointModel> optionalPointModel = Optional.ofNullable(pointDAO.findById(id));
            if (optionalPointModel.isEmpty()) {
                throw new NotFoundException("Not found point with id: " + id);
            }
            pointDAO.delete(id);
        }
    }

    @Override
    public  PointModel getPoint(String studentId, String courseId) {
        List<PointModel> pointModelList = pointDAO.findAll();
        for(PointModel pointModel : pointModelList) {
            if (Objects.equals(pointModel.getStudentId(), studentId) && Objects.equals(pointModel.getCourseId(), courseId))
                return pointModel;
        }
        return null;
    }

    @Override
    public double getAveragePoint(String studentId, String courseId) {
        Optional<PointModel> optionalPointModel = Optional.ofNullable(getPoint(studentId, courseId));
        if (optionalPointModel.isEmpty()) {
            return 0;
        }
        PointModel pointModel = optionalPointModel.get();
        Optional<CourseModel> optionalCourseModel = Optional.ofNullable(courseDAO.findById(pointModel.getCourseId()));
        if (optionalCourseModel.isEmpty()) {
            return 0;
        }
        CourseModel courseModel = optionalCourseModel.get();
        double averagePoint = pointModel.getPointBT() * courseModel.getPercentBT()
                + pointModel.getPointBTL() * courseModel.getPercentBTL()
                + pointModel.getPointGK() * courseModel.getPercentGK()
                + pointModel.getPointCK() * courseModel.getPercentCK();
        return (averagePoint / 100);
    }

    @Override
    public  List<PointModel> getListPointByStudentId(String studentId) throws NotFoundException{
        List<PointModel> pointModelList = pointDAO.findBy("studentId", studentId, Options.OptionBuilder.Builder().setEqual().build());
        if (pointModelList.isEmpty()) {
            throw new NotFoundException("Not found point of student with id: " + studentId);
        }
        else {
            return pointModelList;
        }
//        List<PointModel> pointModelList = pointDAO.findAll();
//        List<PointModel> pointModelList1= new ArrayList<>();
//        for(PointModel pointModel : pointModelList) {
//            if (Objects.equals(pointModel.getStudentId(), studentId))
//                pointModelList1.add(pointModel);
//        }
//        return  pointModelList1;
    }

    @Override
    public List<PointModel> getListStudentByClassId(String classId) throws NotFoundException {
        List<PointModel> pointModelList = pointDAO.findBy("classId", classId, Options.OptionBuilder.Builder().setEqual().build());
        if (pointModelList.isEmpty()) {
            throw new NotFoundException("Not found point of student with id: " + classId);
        }
        else {
            return pointModelList;
        }
//        List<PointModel> pointModelList = pointDAO.findAll();
//        List<PointModel> pointModelList1= new ArrayList<>();
//        for(PointModel pointModel : pointModelList) {
//            if (Objects.equals(pointModel.getClassId(), classId))
//                pointModelList1.add(pointModel);
//        }
//        return  pointModelList1;
    }

    @Override
    public List<PointModel> getListStudentByCourseId(String courseId) throws NotFoundException {
        List<PointModel> pointModelList = pointDAO.findBy("courseId", courseId, Options.OptionBuilder.Builder().setEqual().build());
        if (pointModelList.isEmpty()) {
            throw new NotFoundException("Not found point of student with id: " + courseId);
        }
        else {
            return pointModelList;
        }
//        List<PointModel> pointModelList = pointDAO.findAll();
//        List<PointModel> pointModelList1= new ArrayList<>();
//        for(PointModel pointModel : pointModelList) {
//            if (Objects.equals(pointModel.getCourseId(), courseId))
//                pointModelList1.add(pointModel);
//        }
//        return  pointModelList1;
    }

}
