package com.hcmutap.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PointDTO {
	private String studentId;;
	private double pointBT;
	private double pointBTL;
	private double pointGK;
	private double pointCK;
}
