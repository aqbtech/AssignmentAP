package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.service.IExcelService;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExcelService<T> implements IExcelService<T> {
	@Override
	public Optional<List<T>> readAndConvert(MultipartFile file, Class<T> clazz) {
		Optional<List<T>> result = Optional.empty();
		List<T> list = new ArrayList<>();

		return result;
	}
}
