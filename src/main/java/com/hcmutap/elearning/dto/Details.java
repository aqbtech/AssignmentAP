package com.hcmutap.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
public class Details {
	private String username;
	private List<String> errors;
	private Details() {
	}
	public Details(String username) {
		this.username = username;
		errors = new ArrayList<>();
	}

	public void addError(String error) {
		errors.add(error);
	}
	public boolean hasError() {
		return !errors.isEmpty();
	}
}
