package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.impl.UserDAO;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
	@Resource
	private UserDAO userDAO;
	@Override
	public String save(UserModel userModel) {
		return userDAO.save(userModel);
	}

	@Override
	public void update(UserModel userModel) {
		userDAO.update(userModel);
	}

	@Override
	public void delete(String username) {
		// TODO: find id of user by username or delete by username
		List<UserModel> userModels = userDAO.findBy("username", username);
		if (!userModels.isEmpty()) {
			userDAO.delete(userModels.getFirst().getId());
		}
		else {
			// handle not found exception
		}
	}
}
