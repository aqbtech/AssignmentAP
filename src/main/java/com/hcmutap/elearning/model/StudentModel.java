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
@FirebaseCollection("students")
public class StudentModel {
	@DocumentId
	private String id;
	private String fullName;
	private String username;
	private String email;
	private Long age;
	private int citizen_identityID;
	private String gender;
	private String major;
	private List<String> classes;
	private List<String> courses;
	private List<String> finished_courses;
}