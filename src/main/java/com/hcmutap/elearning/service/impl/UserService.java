package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.repository.UserRepository;
import com.hcmutap.elearning.repository.document.UserDocument;
import com.hcmutap.elearning.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
	@Resource
	private UserRepository userRepository;
	@Override
	public void save(UserModel userModel) {
		UserDocument userDocument = new UserDocument();
		// write function to convert userModel to userDocument
		userDocument.setId(userModel.getId());
		userDocument.setUsername(userModel.getUsername());
		userDocument.setPassword(userModel.getPassword());
		userDocument.setRole(userModel.getRole());
		userDocument.setStatus(userModel.getStatus());
		userDocument.setUsrId(userModel.getUsrId());
		userRepository.push(userDocument);
	}

	@Override
	public void update(UserModel userModel) {
		UserDocument userDocument = new UserDocument();
		// write function to convert userModel to userDocument
		userDocument.setId(userModel.getId());
		userDocument.setUsername(userModel.getUsername());
		userDocument.setPassword(userModel.getPassword());
		userDocument.setRole(userModel.getRole());
		userDocument.setStatus(userModel.getStatus());
		userDocument.setUsrId(userModel.getUsrId());
		userRepository.update(userDocument);
	}

	@Override
	public void delete(String username) {
		// TODO: find id of user by username or delete by username
		userRepository.remove(username);
	}
}
