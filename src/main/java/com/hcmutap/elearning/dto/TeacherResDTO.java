package com.hcmutap.elearning.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResDTO {
//	{
//			"username": "abc",
//			"password": "abc",
//			"role": "TEACHER",
//			"fullName": "Nguyen Van A",
//			"id": "CB0001",
//			"age": 26,
//			"email": "nva@hcmut.edu.vn",
//			"degree": "Ph.D"
//	}
	private String username;
	private String password;
	private String role;
	private String fullName;
	private String id;
	private Long age;
	private String email;
	private String degree;
}
