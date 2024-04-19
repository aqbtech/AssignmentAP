package com.hcmutap.elearning.service;

import com.hcmutap.elearning.exception.NotFoundException;
import org.springframework.ui.ModelMap;

import java.util.List;

public interface IRegisterService {
	String register(ModelMap model) throws NotFoundException;
	String register(List<ModelMap> models) throws NotFoundException;
}
