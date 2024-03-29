package com.hcmutap.elearning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassModel {
    private String id;
    private String classId;
    private String className;
    private String courseId;
    private String teacherId;
    private String teacherName;
    private String dayOfWeek;
    private String timeStart;
    private String timeEnd;
    private String room;
}
