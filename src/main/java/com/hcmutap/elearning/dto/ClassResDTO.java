package com.hcmutap.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassResDTO {
    private String classId;
    private String className;
    private String courseId;
    private String dayOfWeek;
    private String timeStart;
    private int timeStudy;
    private String room;
    private String semester;
    private String infoId;
}