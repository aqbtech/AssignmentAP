package com.hcmutap.elearning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherModel {
	private String id;
	private String teacherId;
	private String name;
	// cast user information to user model before save to database
//	private UserModel account;
	// ds lop
}
