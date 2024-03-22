package com.hcmutap.elearning.dao;

import com.hcmutap.elearning.model.StudentModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class StudentDAO extends AbstractDAO<StudentModel>{
    public List<StudentModel> findAll(){
        List<StudentModel> studentModelList = null;
        try{
            studentModelList = query("students", StudentModel.class);
        }
        catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        return studentModelList;
    }
    public String save(StudentModel studentModel){
        return create("students", studentModel);
    }
    public StudentModel update(StudentModel studentModel){
        List<String> ids = null;
        try{
            ids = findDocument("students","id", studentModel.getStudentId());
        }
        catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        assert ids != null;
        return update("students", ids.getFirst(), studentModel);
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
    public StudentModel findById(String id){
        StudentModel studentModel = null;
        try{
            studentModel = queryBy("students", "id", id, StudentModel.class).getFirst();
        }
        catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        return studentModel;
    }
}
