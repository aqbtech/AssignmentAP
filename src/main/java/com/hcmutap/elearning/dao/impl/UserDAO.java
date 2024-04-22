package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.model.UserModel;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO extends DefaultFirebaseDatabase<UserModel, String> {
}
