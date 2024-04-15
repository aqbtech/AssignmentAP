package com.hcmutap.elearning.model;

import com.hcmutap.elearning.dao.firebase.DocumentId;
import com.hcmutap.elearning.dao.firebase.FirebaseCollection;
import com.hcmutap.elearning.dao.firebase.SecondaryId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FirebaseCollection("classes")
public class ClassModel {
    @DocumentId
    private String firebaseId;
    @SecondaryId
    private String classId;
    private String className;
    private String courseId;
    private String teacherId;
    private String teacherName;
    private String dayOfWeek;
    private String timeStart;
    private String timeEnd;
    private String room;
    private String semesterId;
    private String infoId;
}
