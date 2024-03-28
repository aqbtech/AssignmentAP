package com.hcmutap.elearning.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassModel {
    private String classId;
    private String name;
    private String courseId;
    private String teacherId;
    private String teacherName;
    private String dayOfWeek;
    private String timeStart;
    private String timeEnd;
    private String room;
}
