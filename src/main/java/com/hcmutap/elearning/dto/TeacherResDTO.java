package com.hcmutap.elearning.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResDTO {
	@ColName("Tên đăng nhập")
	private String username;
	@ColName("Mật khẩu")
	private String password;
	@ColName("Role")
	private String role;
	@ColName("Họ tên")
	private String fullName;
	@ColName("ID")
	private String id;
	@ColName("Tuổi")
	private Long age;
	private String email;
	@ColName("Bằng cấp")
	private String degree;
	@ColName("Giới tính")
	private String gender;
}
