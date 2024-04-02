package com.hcmutap.elearning.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.ModelMap;

import java.util.Map;

public class MapperUtil {
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
	public <T> ModelMap toModelMapFromDTO(T dto) {
		try {
			Map<String, Object> map = new ObjectMapper().convertValue(dto, new TypeReference<Map<String, Object>>() {});
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
