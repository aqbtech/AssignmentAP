package com.hcmutap.elearning.dao;

import com.hcmutap.elearning.model.UserModel;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO extends AbstractDAO<UserModel>{
	public UserModel findByUsername(String username) {
		UserModel userModel = null;
		try {
			userModel = queryBy("users","username", username, UserModel.class).getFirst();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return userModel;
	}
}
