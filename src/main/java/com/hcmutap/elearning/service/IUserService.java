package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.model.UserModel;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IUserService {
	String save(UserModel userModel);
	void update(UserModel userModel);
	void delete(String username);
	boolean isExist(String username);
	List<UserModel> findByUsername(String username);
	InfoDTO getInfo(String username);
}
