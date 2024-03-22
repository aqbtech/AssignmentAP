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
public class CourseModel {
    private String id;
    private String courseId;
    private String courseName;
    private int credit;
    private String description;
    private List<ClassModel> classList;
}
