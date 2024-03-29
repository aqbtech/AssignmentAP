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
	private String studentId;
	private String name;
	private int citizen_identityID;
	private String gender;
	private String major;
	private List<ClassModel> classes;
	private List<CourseModel> courses;
	private List<CourseModel> finished_courses;
	public StudentModel(String id, String studentId, String name, int citizen_identityID, String gender, String major){
		this.id = id;
		this.studentId = studentId;
		this.name = name;
		this.citizen_identityID = citizen_identityID;
		this.gender = gender;
		this.major = major;
		this.classes = null;
		this.courses = null;
		this.finished_courses = null;
	}

}