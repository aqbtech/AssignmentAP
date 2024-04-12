package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.UserDAO;
import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IUserService;
import com.hcmutap.elearning.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
	private final UserDAO userDAO;
	private final TeacherService teacherService;
	private final StudentService studentService;

	@Autowired
	public UserService(UserDAO userDAO, TeacherService teacherService, StudentService studentService) {
		this.userDAO = userDAO;
		this.teacherService = teacherService;
		this.studentService = studentService;
	}

	@Override
	public List<UserModel> findAll() {
		return userDAO.findAll();
	}

	@Override
	public List<UserModel> findBy(String key, String value) throws NotFoundException {
		Optional<List<UserModel>> userModels = Optional.ofNullable(userDAO.findBy(key, value, Options.OptionBuilder.Builder().setEqual().build()));
		if (userModels.isPresent() && !userModels.get().isEmpty()) {
			return userModels.get();
		} else {
			throw new NotFoundException("Username not found");
		}
	}

	@Override
	public UserModel findById(String id) throws NotFoundException {
		Optional<UserModel> userModel = Optional.ofNullable(userDAO.findBy("id", id, Options.OptionBuilder.Builder().setEqual().build()).getFirst());
		if (userModel.isPresent()) {
			return userModel.get();
		} else {
			throw new NotFoundException("Username not found");
		}
	}

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
	public void update(UserModel userModel) throws NotFoundException {
		List<UserModel> userModels = userDAO.findBy("username", userModel.getUsername());
		if (userModels.isEmpty()) {
			throw new NotFoundException("Username not found");
		} else {
			userDAO.update(userModel);
		}
	}

	@Override
	public void delete(List<String> ids) throws NotFoundException {
		for (String id : ids) {
			userDAO.delete(id);
		}
	}

	@Override
	public void delete(String username) throws NotFoundException {
		List<UserModel> userModels = userDAO.findBy("username", username);
		if (!userModels.isEmpty()) {
			userDAO.delete(userModels.getFirst().getFirebaseId());
		} else {
			throw new NotFoundException("Username not found");
		}
	}

	@Override
	public boolean isExist(String username) {
		return !userDAO.findBy("username", username).isEmpty();
	}

	@Override
	public UserModel findByUsername(String username) throws NotFoundException {
		List<UserModel> userModels = userDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build());
		if (userModels.isEmpty()) {
			throw new NotFoundException("Username not found");
		}
		return userModels.getFirst();
	}

	@Override
	public InfoDTO getInfo(String username) throws NotFoundException {
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
			else {
				InfoDTO infoDTO = MapperUtil.getInstance().toDTOFromModel(userModel, InfoDTO.class);
				infoDTO.setRole("admin");
				return infoDTO;
			}
		}
		throw new NotFoundException("Username not found");
	}
}
