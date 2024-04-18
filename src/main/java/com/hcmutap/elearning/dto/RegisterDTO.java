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
	@ColName("Username")
	private String username;
	@ColName("Password")
	private String password;
	@ColName("Role")
	private String role;
	@ColName("Họ tên")
	private String fullName;
	@ColName("ID")
	private String id;
	@ColName("Tuổi")
	private Long age;
	@ColName("Email")
	private String email;
	@ColName(value = "Bằng cấp", optional = true)
	private String degree;
	@ColName("Giới tính")
	private String gender;
	@ColName(value = "Chuyên ngành", optional = true)
	private String major;
}
