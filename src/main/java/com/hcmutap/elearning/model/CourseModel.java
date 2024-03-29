package com.hcmutap.elearning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseModel {
    private String courseId;
    private String courseName;
    private int credit;
    private int percentBT;
    private int percentBTL;
    private int percentGK;
    private int percentCK;
}
