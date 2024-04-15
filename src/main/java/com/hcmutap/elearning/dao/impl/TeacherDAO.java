package com.hcmutap.elearning.dao.impl;


import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.model.TeacherModel;
import org.springframework.stereotype.Repository;

@Repository("teacherDAO")
public class TeacherDAO extends DefaultFirebaseDatabase<TeacherModel,String> {
}
