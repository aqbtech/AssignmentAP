package com.hcmutap.elearning.service;

import com.hcmutap.elearning.exception.NotFoundException;
import org.springframework.ui.ModelMap;

public interface IRegisterService {
	String register(ModelMap model) throws NotFoundException;
}
