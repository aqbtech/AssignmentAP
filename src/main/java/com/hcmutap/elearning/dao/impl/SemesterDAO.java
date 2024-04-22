package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.model.SemesterModel;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class SemesterDAO extends DefaultFirebaseDatabase<SemesterModel,String> {
}
