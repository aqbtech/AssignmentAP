package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.model.CourseModel;
import org.springframework.stereotype.Repository;


@Repository
public class CourseDAO extends DefaultFirebaseDatabase<CourseModel, String>{
//    public List<CourseModel> findAll() {
//        List<CourseModel> CourseModelList = null;
//        try {
//            CourseModelList = query("courses", CourseModel.class);
//        }
//        catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return CourseModelList;
//    }
//    public String save(CourseModel courseModel) {
//        return create("courses", courseModel);
//    }
//    public CourseModel update(CourseModel courseModel) {
//        List<String> ids = null;
//        try {
//            ids = findDocument("courses", "courseId", courseModel.getCourseId());
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        assert ids != null; // handle not found exception
//        return update("courses", ids.getFirst(), courseModel);
//    }
//    public void delete(String courseId) {
//        List<String> ids = null;
//        try {
//            ids = findDocument("courses", "courseId", courseId);
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        assert ids != null; // handle not found exception
//        delete("courses", ids.getFirst());
//    }
    public CourseModel findById(String courseId) {
        return findBy("courseId", courseId, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
    }
}
