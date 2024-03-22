package com.hcmutap.elearning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassStudentListModel {
    private String id;
    private String classId;
    private String className;
    private String studentId;
    private String studentName;
}
