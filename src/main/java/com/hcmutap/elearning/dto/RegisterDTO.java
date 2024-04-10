package com.hcmutap.elearning.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
	@Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "Username must be 6-20 characters and no special characters")
	private String username;
	@NotBlank(message = "Password is required dto")
	@Size(min = 6, max = 20, message = "Password must be 6-20 characters")
	private String password;
	private String role;
	private String fullName;
	private String id;
	private Long age;
	private String email;
	private String degree;
	private String gender;
	private String major;
}
