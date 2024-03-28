package com.hcmutap.elearning.dao;

import com.hcmutap.elearning.model.CourseModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;
@Repository
public class CourseDAO extends AbstractDAO<CourseModel>{
    public List<CourseModel> findAll(){
        List<CourseModel> courseModelList = null;
        try{
            courseModelList = query("students", CourseModel.class);
        }
        catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        return courseModelList;
    }
    public String save(CourseModel CourseModel){
        return create("students", CourseModel);
    }
    public CourseModel update(CourseModel CourseModel){
        List<String> ids = null;
        try{
            ids = findDocument("students","id", CourseModel.getCourseID());
        }
        catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        assert ids != null;
        return update("students", ids.getFirst(), CourseModel);
    }
    public void delete(String id){
        List<String> ids = null;
        try{
            ids = findDocument("students", "id", id);
        }
        catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        assert ids != null;
        delete("students", ids.getFirst());
    }
    public CourseModel findById(String id){
        CourseModel courseModel = null;
        try{
            courseModel = queryBy("students", "id", id, CourseModel.class).getFirst();
        }
        catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        return courseModel;
    }
}
