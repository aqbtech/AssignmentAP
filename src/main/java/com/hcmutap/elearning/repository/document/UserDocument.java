package com.hcmutap.elearning.repository.document;

import com.github.fabiomaffioletti.firebase.document.FirebaseDocument;
import com.github.fabiomaffioletti.firebase.document.FirebaseId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FirebaseDocument("/users")
public class UserDocument {
	@FirebaseId
	private String id;
	private String username;
	private String password;
	private String role;
	private Long Status;
//	private String email;
	private String usrId; // to store id of teacher or student
}
