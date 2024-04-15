package com.hcmutap.elearning.service;

import com.hcmutap.elearning.dto.PointDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;

import java.util.List;

public interface ICourseFacade {
	//course service
	CourseModel getCourseInfo(String courseId) throws NotFoundException;
	List<ClassModel> getLichTrinh(String courseId) throws NotFoundException;
	List<PointModel> getListPointOfStudent(String courseId) throws NotFoundException;
	//class service
	ClassModel getClassInfo(String classId) throws NotFoundException;
	List<ClassModel> getClassOfCourse(String courseId);
	List<PointModel> getListStudentOfClass(String classId) throws NotFoundException;
	List<ClassModel> getTimeTableSV(String studentId);
	List<ClassModel> getTimeTableGV(String teacherId);
	boolean addStudentToClass(String studentId, String classId) throws NotFoundException;
	boolean NhapDiem(String studentId, String classId, PointDTO point) throws NotFoundException;
	void NhapDiemCaLop(String classId, List<PointDTO> listPoint) throws NotFoundException;
	//point service
	PointModel getPoint(String studentID, String courseID);
	double getAveragePoint(String studentID, String courseID);
	List<PointModel> getListStudentOfCourse(String courseID) throws NotFoundException;
	//--------------------------
	ClassModel findClassById(String classId) throws NotFoundException;
}
