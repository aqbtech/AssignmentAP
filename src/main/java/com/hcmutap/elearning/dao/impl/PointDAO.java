package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.model.PointModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointDAO extends DefaultFirebaseDatabase<PointModel, String>{
    public List<PointModel> findPoint(String studentId) {
        return findBy("studentId", studentId, Options.OptionBuilder.Builder().setEqual().build());
    }
}
