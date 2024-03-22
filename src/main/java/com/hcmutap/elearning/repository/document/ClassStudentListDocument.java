package com.hcmutap.elearning.repository.document;

import com.github.fabiomaffioletti.firebase.document.FirebaseDocument;
import com.github.fabiomaffioletti.firebase.document.FirebaseId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@FirebaseDocument("/class-student-list")
public class ClassStudentListDocument {
    @FirebaseId
    private String id;
    private String classId;
    private String className;
    private String studentId;
    private String studentName;
}
