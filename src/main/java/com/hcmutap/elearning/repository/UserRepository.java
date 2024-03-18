package com.hcmutap.elearning.repository;

import com.github.fabiomaffioletti.firebase.repository.DefaultFirebaseRealtimeDatabaseRepository;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.repository.document.UserDocument;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends DefaultFirebaseRealtimeDatabaseRepository<UserDocument, String> {
	public UserModel findByUsername(String username){
		List<UserDocument> userDocuments = findAll();
		// find user by username in list
		for (UserDocument userDocument : userDocuments){
			if (userDocument.getUsername().equals(username)){
				UserModel userModel = new UserModel();
				userModel.setId(userDocument.getId());
				userModel.setUsername(userDocument.getUsername());
				userModel.setPassword(userDocument.getPassword());
				userModel.setRole(userDocument.getRole());
				userModel.setStatus(userDocument.getStatus());
				userModel.setUsrId(userDocument.getUsrId());
				return userModel;
			}
		}
		return null;
	}
}
