package com.hcmutap.elearning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
	private String id;
	private String username;
	private String password;
	private String role;
	private Long Status;
	private String usrId; // to store id of teacher or student
}
