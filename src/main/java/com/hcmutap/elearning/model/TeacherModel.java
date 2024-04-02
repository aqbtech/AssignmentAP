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
	// foreign key of user for get data in users collection
	@DocumentId
	private String id;
	private String fullName;
	private Long age;
	private String username;
	private String email;
	private String degree;
	private TimetableModel timetable;
	private List<String> courses; // save id of courses
	List<String> classes; // save id of classes
}
