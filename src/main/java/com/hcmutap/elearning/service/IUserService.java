package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.UserModel;
public interface IUserService {
	public String save(UserModel userModel);
	public void update(UserModel userModel);
	public void delete(String username);
}
