package com.hcmutap.elearning.validator;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.UserDAO;
import com.hcmutap.elearning.dto.RegisterDTO;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.impl.UserService;
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

		if (!emailValidator.isValid(registerDTO.getEmail(), null)) {
			errors.rejectValue("email", "Pattern.registerForm.email");
		} // TODO:
		else if(registerDTO.getUsername() != null) {
			List<UserModel> userModels = userDAO.findBy("email", registerDTO.getEmail(), Options.OptionBuilder.Builder().setEqual().build());
			if(!userModels.isEmpty()) {
				errors.rejectValue("email", "Duplicate.registerForm.email");
			}
		}

		if (registerDTO.getRole().equalsIgnoreCase("STUDENT")) {
			if (!registerDTO.getId().matches("^\\d{7}$")) {
				errors.rejectValue("id", "Pattern.registerForm.id.student");
			}
		}
		if (registerDTO.getRole().equalsIgnoreCase("TEACHER")) {
			if (!registerDTO.getId().matches("^CB\\d{4}$")) {
				errors.rejectValue("id", "Pattern.registerForm.id.teacher");
			}
		}

		if(!errors.hasFieldErrors("id")) {
			List<UserModel> userModels = userDAO.findBy("usrId", registerDTO.getId(), Options.OptionBuilder.Builder().setEqual().build());
			if(!userModels.isEmpty()) {
				errors.rejectValue("id", "Duplicate.registerForm.id");
			}
		}

		if (!errors.hasFieldErrors("username")) {
			if (userService.isExist(registerDTO.getUsername())) {
				errors.rejectValue("username", "Duplicate.registerForm.username");
			}
		}
	}
}
