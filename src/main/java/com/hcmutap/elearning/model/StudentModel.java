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
@FirebaseCollection("students")
public class StudentModel extends AbstractModel {
	@DocumentId
	private String id;
	private String fullName;
	private Long age;
	private String email;
	private String username;
	private String studentId;
	private String name;
}
