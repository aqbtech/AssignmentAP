package com.hcmutap.elearning.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class MapperUtil {
	private static MapperUtil mapperUtil = null;

	public static MapperUtil getInstance() {
		if (mapperUtil == null) {
			mapperUtil = new MapperUtil();
		}
		return mapperUtil;
	}

	public <T> T toModel(String value, Class<T> tClass) {
		try {
			return new ObjectMapper().readValue(value, tClass);
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return null;
	}
	public String toJson(Object object) {
		try {
			return new ObjectMapper().writeValueAsString(object);
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return null;
	}
	public Map<String,?> toMap(Object object) {
		try {
			return new ObjectMapper().convertValue(object, new TypeReference<Map<String, Object>>() {});
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return null;
	}
}
