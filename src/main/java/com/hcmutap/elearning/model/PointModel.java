package com.hcmutap.elearning.model;

import com.hcmutap.elearning.dao.firebase.DocumentId;
import com.hcmutap.elearning.dao.firebase.FirebaseCollection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FirebaseCollection("points")
public class PointModel {
   @DocumentId
   private String firebaseId;
   private String id; // timestamp
   private String studentId;
   private String studentName;
   private String courseId;
   private String courseName;
   private String classId;
   private String className;
   private String semesterId;
   private boolean state;
   private double pointBT;
   private double pointBTL;
   private double pointGK;
   private double pointCK;
}
