package com.hcmutap.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfoDTO {
	private String id;
	private String fullName;
	private String role;
	private Long age;
	private String email;
	private String degree;
}
