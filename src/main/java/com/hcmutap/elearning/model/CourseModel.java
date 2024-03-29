package com.hcmutap.elearning.model;

import com.hcmutap.elearning.dao.firebase.DocumentId;
import com.hcmutap.elearning.dao.firebase.FirebaseCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FirebaseCollection("courses")
public class CourseModel {
    @DocumentId
    private String courseId;
    private String courseName;
    private int credit;
    private int percentBT;
    private int percentBTL;
    private int percentGK;
    private int percentCK;
}
