package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.RegisterDTO;
import com.hcmutap.elearning.exception.ConvertExcelToObjectException;
import com.hcmutap.elearning.exception.MappingException;
import com.hcmutap.elearning.exception.NotFoundException;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IRegisterService {
	String register(ModelMap model) throws NotFoundException, MappingException;
	Map<String, String> register(MultipartFile file) throws ConvertExcelToObjectException, MappingException;
	String deleteAccount(String id, String type);
	String updateAccount(RegisterDTO form, String type);
}
