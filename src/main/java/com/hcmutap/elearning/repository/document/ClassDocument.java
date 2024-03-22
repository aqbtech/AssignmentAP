package com.hcmutap.elearning.repository.document;

import com.github.fabiomaffioletti.firebase.document.FirebaseDocument;
import com.github.fabiomaffioletti.firebase.document.FirebaseId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@FirebaseDocument("/classes")
public class ClassDocument {
    @FirebaseId
    private String id;
    private String classId;
    private String className;
    private String courseId;
    private String courseName;
    private String teacherId;
    private String teacherName;
    private String room;
    private String time;
}
