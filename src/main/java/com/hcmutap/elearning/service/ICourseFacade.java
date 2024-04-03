package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.PointDTO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;

import java.util.List;

public interface ICourseFacade {
	//course service
	CourseModel getCourseInfo(String courseId);
	List<ClassModel> getLichTrinh(String courseId);
	List<PointModel> getListPointOfStudent(String courseId);
	//class service
	ClassModel getClassInfo(String classId);
	List<ClassModel> getClassOfCourse(String courseId);
	List<PointModel> getListStudentOfClass(String classId);
	List<ClassModel> getTimeTableSV(String studentId);
	List<ClassModel> getTimeTableGV(String teacherId);
	boolean addStudentToClass(String studentId, String classId);
	void NhapDiem(String studentId, String classId, PointDTO point);
	void NhapDiemCaLop(String classId, List<PointDTO> listPoint);
	//point service
	PointModel getPoint(String studentID, String courseID);
	double getAveragePoint(String studentID, String courseID);
	List<PointModel> getListClassOfStudent(String studentID);
	List<PointModel> getListStudentOfCourse(String courseID);
	//--------------------------
	public ClassModel findClassById(String classId);
}
