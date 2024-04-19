package com.hcmutap.elearning.validator;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.UserDAO;
import com.hcmutap.elearning.dto.RegisterDTO;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.impl.UserService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class RegisterDTOValidator implements Validator {
	private EmailValidator emailValidator = new EmailValidator();
	private UserDAO userDAO;
	private UserService userService;
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == RegisterDTO.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		RegisterDTO registerDTO = (RegisterDTO) target;

		if (registerDTO.getUsername() == null || !registerDTO.getUsername().matches("^[a-zA-Z0-9]{6,20}$")) {
			errors.rejectValue("username", "Pattern.registerForm.username");
		} else if (userService.isExist(registerDTO.getUsername())) {
			errors.rejectValue("username", "Duplicate.registerForm.username");
		}

		if (registerDTO.getPassword() == null || registerDTO.getPassword().length() < 6 || registerDTO.getPassword().length() > 20) {
			errors.rejectValue("password", "Size.registerForm.password");
		}

		if (registerDTO.getRole() == null || !registerDTO.getRole().matches("^(?i)(teacher|student)$")) {
			errors.rejectValue("role", "Pattern.registerForm.role");
		}

		if (registerDTO.getFullName() == null) {
			errors.rejectValue("fullName", "NotBlank.registerForm.fullName");
		}

		if (registerDTO.getId() == null) {
			errors.rejectValue("id", "NotBlank.registerForm.id");
		} else if (registerDTO.getRole() != null && registerDTO.getRole().equalsIgnoreCase("STUDENT") && !registerDTO.getId().matches("^\\d{7}$")) {
			errors.rejectValue("id", "Pattern.registerForm.id.student");
		} else if (registerDTO.getRole() != null && registerDTO.getRole().equalsIgnoreCase("TEACHER") && !registerDTO.getId().matches("^CB\\d{4}$")) {
			errors.rejectValue("id", "Pattern.registerForm.id.teacher");
		} else {
			List<UserModel> userModels = userDAO.findBy("usrId", registerDTO.getId(), Options.OptionBuilder.Builder().setEqual().build());
			if(!userModels.isEmpty()) {
				errors.rejectValue("id", "Duplicate.registerForm.id");
			}
		}

		if (registerDTO.getAge() == null || registerDTO.getAge() < 18 || registerDTO.getAge() > 65) {
			errors.rejectValue("age", "Range.registerForm.age");
		}

		if (registerDTO.getEmail() == null || !emailValidator.isValid(registerDTO.getEmail(), null)) {
			errors.rejectValue("email", "Pattern.registerForm.email");
		} else {
			List<UserModel> userModels = userDAO.findBy("email", registerDTO.getEmail(), Options.OptionBuilder.Builder().setEqual().build());
			if(!userModels.isEmpty()) {
				errors.rejectValue("email", "Duplicate.registerForm.email");
			}
		}

		if (registerDTO.getDegree() == null && registerDTO.getRole().equalsIgnoreCase("TEACHER")) {
			errors.rejectValue("degree", "NotBlank.registerForm.degree");
		}

		if (registerDTO.getGender() == null || !registerDTO.getGender().matches("^(?i)(Nam|Nữ)$")) {
			errors.rejectValue("gender", "Pattern.registerForm.gender");
		}

		if (registerDTO.getMajor() == null && registerDTO.getRole().equalsIgnoreCase("STUDENT")) {
			errors.rejectValue("major", "NotBlank.registerForm.major");
		}
	}
}
