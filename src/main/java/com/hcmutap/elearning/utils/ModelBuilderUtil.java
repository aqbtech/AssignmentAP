package com.hcmutap.elearning.utils;

import org.springframework.beans.BeanUtils;

public class ModelBuilderUtil {

	public static <T> T buildModelFromDTO(T model, Object dto) {
		BeanUtils.copyProperties(dto, model);
		return model;
	}
}
