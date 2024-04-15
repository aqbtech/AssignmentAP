package com.hcmutap.elearning.dao.impl;


import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.model.StudentModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("studentDAO")
public class StudentDAO extends DefaultFirebaseDatabase<StudentModel, String> {
}
