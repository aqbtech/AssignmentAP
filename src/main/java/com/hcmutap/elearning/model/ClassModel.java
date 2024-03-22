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
public class ClassModel {
    private String id;
    private String classId;
    private String className;
    private String courseId;
    private String courseName;
    private String teacherId;
    private String teacherName;
    private String room;
    private String time;
    private List<ClassStudentListModel> classStudentList;
}
