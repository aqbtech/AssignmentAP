package com.hcmutap.elearning.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResDTO {
	@ColName("ID")
	private String id;
	@ColName("Họ tên")
	private String fullName;
	@ColName("Email")
	private String email;
	@ColName("Tuổi")
	private Long age;
	@ColName("Tên đăng nhập")
	private String username;
	@ColName("Mật khẩu")
	private String password;
	@ColName("Role")
	private String role;
	@ColName("Giới tính")
	private String gender;
	@ColName("Khoa")
	private String major;
}
