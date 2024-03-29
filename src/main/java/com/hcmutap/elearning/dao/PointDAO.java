package com.hcmutap.elearning.dao;

import com.hcmutap.elearning.model.PointModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class PointDAO extends AbstractDAO<PointModel> {
    public List<PointModel> findAll() {
        List<PointModel> pointModelList = null;
        try {
            pointModelList = query("points", PointModel.class);
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return pointModelList;
    }
    public String save(PointModel pointModel) {
        return create("points", pointModel);
    }

    public PointModel update(PointModel pointModel) {
        List<String> ids = null;
        try {
            ids = findDocument("points", "id", pointModel.getId());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        assert ids != null; // handle not found exception
        return update("points", ids.getFirst(),pointModel);
    }
    public void delete(String id) {
        List<String> ids = null;
        try {
            ids = findDocument("points", "id", id);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        assert ids != null; // handle not found exception
        delete("points", ids.getFirst());
    }
    public PointModel findById(String id) {
        PointModel pointModel = null;
        try {
            pointModel = queryBy("points","id", id, PointModel.class).getFirst();
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return pointModel;
    }
}
