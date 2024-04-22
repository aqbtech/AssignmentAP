package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.UserModel;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IUserService extends IGenericAdminService<UserModel> {
	UserModel findByUsername(String username) throws NotFoundException;
	void delete(String username) throws NotFoundException;
	boolean isExist(String username);
	InfoDTO getInfo(String username) throws NotFoundException;
}
