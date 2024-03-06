package com.hcmutap.elearning.repository.document;

import com.github.fabiomaffioletti.firebase.document.FirebaseDocument;
import com.github.fabiomaffioletti.firebase.document.FirebaseId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@FirebaseDocument("/teachers")
public class TeacherDocument {
	@FirebaseId
	private String id;
	private String teacherId;
	private String name;
}
