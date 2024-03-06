package com.hcmutap.elearning.repository.document;

import com.github.fabiomaffioletti.firebase.document.FirebaseDocument;
import com.github.fabiomaffioletti.firebase.document.FirebaseId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@FirebaseDocument("/students")
public class StudentDocument {
	@FirebaseId
	private String id;
	private String studentId;
	private String name;

}
