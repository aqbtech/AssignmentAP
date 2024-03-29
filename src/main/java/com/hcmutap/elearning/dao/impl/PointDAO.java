package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.model.PointModel;
import org.springframework.stereotype.Repository;

@Repository
public class PointDAO extends DefaultFirebaseDatabase<PointModel, String>{
//    public List<PointModel> findAll() {
//        List<PointModel> pointModelList = null;
//        try {
//            pointModelList = query("points", PointModel.class);
//        }
//        catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return pointModelList;
//    }
//    public String save(PointModel pointModel) {
//        return create("points", pointModel);
//    }
//
//    public PointModel update(PointModel pointModel) {
//        List<String> ids = null;
//        try {
//            ids = findDocument("points", "id", pointModel.getId());
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        assert ids != null; // handle not found exception
//        return update("points", ids.getFirst(),pointModel);
//    }
//    public void delete(String id) {
//        List<String> ids = null;
//        try {
//            ids = findDocument("points", "id", id);
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        assert ids != null; // handle not found exception
//        delete("points", ids.getFirst());
//    }
    public PointModel findById(String id) {
        return findBy("id", id, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
    }
}
