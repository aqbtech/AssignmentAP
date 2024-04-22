package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.RegisterDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IRegisterService {
	String register(ModelMap model) throws NotFoundException;
	Map<String, String> register(MultipartFile file);
	String deleteAccount(String id, String type);
	String updateAccount(RegisterDTO form, String type);
}
