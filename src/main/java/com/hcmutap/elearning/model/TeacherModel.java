package com.hcmutap.elearning.model;

import com.hcmutap.elearning.dao.firebase.DocumentId;
import com.hcmutap.elearning.dao.firebase.FirebaseCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FirebaseCollection("teachers")
public class TeacherModel {
	@DocumentId
	private String firebaseId;
	private String id;
	private String fullName;
	private String gender;
	private Long age;
	private String username;
	private String email;
	private String degree;
	private List<String> classes;
	private List<String> courses;
}
