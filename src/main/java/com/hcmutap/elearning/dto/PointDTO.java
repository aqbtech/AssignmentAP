package com.hcmutap.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PointDTO {
	private String studentId;
	private String CourseId;
	private String ClassId;
	private double pointBT;
	private double pointBTL;
	private double pointGK;
	private double pointCK;
}
