package com.hcmutap.elearning.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResDTO {
	private String id;
	private String fullName;
	private String email;
	private Long age;
	private String username;
	private String password;
	private String role;
}
