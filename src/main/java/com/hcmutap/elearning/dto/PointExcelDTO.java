package com.hcmutap.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PointExcelDTO {
    @ColName(value = "StudentID", optional = true)
    private String studentId;
    @ColName(value = "PointBT", optional = true)
    private double pointBT;
    @ColName(value = "PointBTL", optional = true)
    private double pointBTL;
    @ColName(value = "PointGK", optional = true)
    private double pointGK;
    @ColName(value = "PointCK", optional = true)
    private double pointCK;
}
