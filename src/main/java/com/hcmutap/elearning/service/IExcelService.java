package com.hcmutap.elearning.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IExcelService<T> {
	Optional<List<T>> readAndConvert(MultipartFile file, Class<T> clazz);
}
