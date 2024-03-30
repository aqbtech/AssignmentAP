package com.hcmutap.elearning.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
	private String courseId;
	private String courseName;
	private int credit;
	private int percentBT;
	private int percentBTL;
	private int percentGK;
	private int percentCK;
	private String role;
}
