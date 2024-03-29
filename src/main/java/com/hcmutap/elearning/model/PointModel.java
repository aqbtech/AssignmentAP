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
@FirebaseCollection("points")
public class PointModel {
   @DocumentId
   private String id;
   private String studentId;
   private String studentName;
   private String courseId;
   private String classId;
   private boolean state;
   private double pointBT;
   private double pointBTL;
   private double pointGK;
   private double pointCK;

}
