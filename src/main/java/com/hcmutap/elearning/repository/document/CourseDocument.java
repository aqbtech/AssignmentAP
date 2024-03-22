package com.hcmutap.elearning.repository.document;

import com.github.fabiomaffioletti.firebase.document.FirebaseDocument;
import com.github.fabiomaffioletti.firebase.document.FirebaseId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@FirebaseDocument("/courses")
public class CourseDocument {
    @FirebaseId
    private String id;
    private String courseId;
    private String courseName;
    private int credit;
    private String description;
}
