package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.Details;
import com.hcmutap.elearning.dto.RegisterDTO;
import com.hcmutap.elearning.exception.ConvertExcelToObjectException;
import com.hcmutap.elearning.exception.MappingException;
import com.hcmutap.elearning.exception.NotFoundException;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;


public interface IRegisterService {
	String register(ModelMap model) throws NotFoundException, MappingException;
	Details register(MultipartFile file) throws ConvertExcelToObjectException, MappingException;
	String deleteAccount(String id, String type);
	String updateAccount(RegisterDTO form, String type);
}
