package com.hcmutap.elearning.exception;

import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException {
	private final String code;
	public CustomRuntimeException(String message, String code) {
		super(message);
		this.code = code;
	}
}
