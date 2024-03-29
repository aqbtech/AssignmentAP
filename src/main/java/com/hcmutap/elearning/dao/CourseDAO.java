package com.hcmutap.elearning.dao;

import com.hcmutap.elearning.model.CourseModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class CourseDAO extends AbstractDAO<CourseModel> {
    public List<CourseModel> findAll() {
        List<CourseModel> CourseModelList = null;
        try {
            CourseModelList = query("courses", CourseModel.class);
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return CourseModelList;
    }
    public String save(CourseModel courseModel) {
        return create("courses", courseModel);
    }
    public CourseModel update(CourseModel courseModel) {
        List<String> ids = null;
        try {
            ids = findDocument("courses", "courseId", courseModel.getCourseId());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        assert ids != null; // handle not found exception
        return update("courses", ids.getFirst(), courseModel);
    }
    public void delete(String courseId) {
        List<String> ids = null;
        try {
            ids = findDocument("courses", "courseId", courseId);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        assert ids != null; // handle not found exception
        delete("courses", ids.getFirst());
    }
    public CourseModel findById(String courseId) {
        CourseModel courseModel = null;
        try {
            courseModel = queryBy("courses","courseId", courseId, CourseModel.class).getFirst();
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return courseModel;
    }
}
