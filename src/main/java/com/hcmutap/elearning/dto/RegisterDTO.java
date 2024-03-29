package com.hcmutap.elearning.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
	private String username;
	private String password;
	private String role;
	private String fullName;
	private String id;
	private Long age;
	private String email;
	private String degree;
}
