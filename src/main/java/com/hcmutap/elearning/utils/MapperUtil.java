package com.hcmutap.elearning.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmutap.elearning.exception.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import com.hcmutap.elearning.dto.RegisterDTO;
import java.lang.reflect.Field;

import java.util.Map;

public class MapperUtil {
	private final Logger logger = LoggerFactory.getLogger(MapperUtil.class);
	private static MapperUtil mapperUtil = null;

	public static MapperUtil getInstance() {
		if (mapperUtil == null) {
			mapperUtil = new MapperUtil();
		}
		return mapperUtil;
	}
	public <T> T toModelFromModelMap(ModelMap modelMap, Class<T> tClass) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			String jsonString = new ObjectMapper().writeValueAsString(modelMap);
			return mapper.readValue(jsonString, tClass);
		} catch (JsonProcessingException e) {
			System.out.print(e.getMessage());
		}
		return null;
	}
	public <T> void updateModelFromDto(T model, RegisterDTO dto) throws MappingException {
		Field[] dtoFields = dto.getClass().getDeclaredFields();
		Field[] modelFields = model.getClass().getDeclaredFields();

		for (Field dtoField : dtoFields) {
			dtoField.setAccessible(true);
			for (Field modelField : modelFields) {
				modelField.setAccessible(true);
				if (dtoField.getName().equals(modelField.getName())) {
					try {
						Object value = dtoField.get(dto);
						if (value != null && !StringUtils.hasText(value.toString())) {
							modelField.set(model, value);
						}
					} catch (IllegalAccessException e) {
						throw new MappingException(e.getMessage());
					}
				}
			}
		}
	}
	public <T> ModelMap toModelMapFromDTO(T dto) {
		try {
			Map<String, Object> map = new ObjectMapper().convertValue(dto, new TypeReference<>() {});
			ModelMap modelMap = new ModelMap();
			modelMap.addAllAttributes(map);
			return modelMap;
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return null;
	}
	public <T> T toDTOFromModel(Object object, Class<T> tClass) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			String json = mapper.writeValueAsString(object);
			return mapper.readValue(json, tClass);
		} catch (Exception e) {
			System.out.println("Error converting object to DTO: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	public <T> T toModel(String value, Class<T> tClass) throws MappingException {
		try {
			return new ObjectMapper().readValue(value, tClass);
		} catch (Exception e) {
			throw new MappingException(e.getMessage());
		}
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
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return mapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return null;
	}
}
