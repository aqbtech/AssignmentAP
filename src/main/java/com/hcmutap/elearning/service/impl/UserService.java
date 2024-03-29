package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.UserDAO;
import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IUserService;
import com.hcmutap.elearning.utils.MapperUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
	@Resource
	private UserDAO userDAO;
	@Resource
	private TeacherService teacherService;
	@Resource
	private StudentService studentService;
	@Override
	public String save(UserModel userModel) {
		if (userModel.getStatus() == null) {
			userModel.setStatus("active");
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		userModel.setPassword(encoder.encode(userModel.getPassword()));
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

	@Override
	public boolean isExist(String username) {
		return !userDAO.findBy("username", username).isEmpty();
	}

	@Override
	public List<UserModel> findByUsername(String username) {
		return userDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build());
	}

	@Override
	public InfoDTO getInfo(String username) {
		List<UserModel> userModels = userDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build());
		if (!userModels.isEmpty()) {
			UserModel userModel = userModels.getFirst();
			if (userModel.getRole().equalsIgnoreCase("teacher")) {
				TeacherModel teacherModel = teacherService.findByUsername(username);
				InfoDTO infoDTO = MapperUtil.getInstance().toDTOFromModel(teacherModel, InfoDTO.class);
				infoDTO.setRole("teacher");
				return infoDTO;
			}
			else if (userModel.getRole().equalsIgnoreCase("student")) {
				StudentModel studentModel = studentService.findByUsername(username);
				InfoDTO infoDTO = MapperUtil.getInstance().toDTOFromModel(studentModel, InfoDTO.class);
				infoDTO.setRole("student");
				return infoDTO;
			}
		}
		return null;
	}
}
