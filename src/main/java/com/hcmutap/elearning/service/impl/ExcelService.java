package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.exception.ConvertExcelToObjectException;
import com.hcmutap.elearning.service.IExcelService;
import com.hcmutap.elearning.utils.ExcelMapperUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExcelService<T> implements IExcelService<T> {
	@Override
	public Optional<List<T>> readAndConvert(MultipartFile file, Class<T> clazz) throws ConvertExcelToObjectException {
		return ExcelMapperUtil.getInstance().readAndConvert(file, clazz);
	}
}
