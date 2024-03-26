package com.hcmutap.elearning.model;

import com.hcmutap.elearning.dao.firebase.DocumentId;
import com.hcmutap.elearning.dao.firebase.FirebaseCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FirebaseCollection("users")
public class UserModel {
	@DocumentId
	private String id;
	private String username;
	private String password;
	private String role;
	private Long Status;
	private String usrId; // to store id of teacher or student
}
