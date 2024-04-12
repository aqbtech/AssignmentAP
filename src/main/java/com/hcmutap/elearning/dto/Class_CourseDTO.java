package com.hcmutap.elearning.dto;

import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Class_CourseDTO {
    ClassModel lophoc;
    CourseModel course;
}
