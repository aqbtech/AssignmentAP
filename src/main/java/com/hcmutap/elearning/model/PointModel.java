package com.hcmutap.elearning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointModel {
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
