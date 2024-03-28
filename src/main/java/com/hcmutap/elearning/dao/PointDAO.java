package com.hcmutap.elearning.dao;

import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;
@Repository
public class PointDAO extends AbstractDAO<PointModel>{
    public List<PointModel> findPoint(String id){
        List<PointModel> point = null;
        try{
            point = queryBy("students", "id", id, PointModel.class);
        }
        catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        return point;
    }
}
