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
	private UserModel account;
	// ds lop
}
