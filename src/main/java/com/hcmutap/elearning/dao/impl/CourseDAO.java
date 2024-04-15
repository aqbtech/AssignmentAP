package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.model.CourseModel;
import org.springframework.stereotype.Repository;


@Repository
public class CourseDAO extends DefaultFirebaseDatabase<CourseModel, String>{
}
