package com.hcmutap.elearning.model;


import com.hcmutap.elearning.dao.firebase.DocumentId;
import com.hcmutap.elearning.dao.firebase.FirebaseCollection;
import com.hcmutap.elearning.dao.firebase.SecondaryId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FirebaseCollection("semesters")
public class SemesterModel {
	@DocumentId
	private String firebaseId;
	@SecondaryId
	private String id;
	private String semesterName;
	private String startDate;
	private String endDate;
}
