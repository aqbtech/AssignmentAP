package com.hcmutap.elearning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentModel {
	private String id;
	private String studentId;
	private String name;
	private int citizen_identityID;
	private String gender;
	private String major;
	private ScheduleModel schedule;

	public StudentModel(String id, String studentId, String name, int citizen_identityID, String gender, String major){
		this.id = id;
		this.studentId = studentId;
		this.name = name;
		this.citizen_identityID = citizen_identityID;
		this.gender = gender;
		this.major = major;
	}

}
