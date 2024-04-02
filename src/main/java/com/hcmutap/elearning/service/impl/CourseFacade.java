package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dto.PointDTO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.ICourseFacade;
import lombok.Getter;

import java.util.List;

public class CourseFacade implements ICourseFacade {
	@Getter
	private static final CourseFacade INSTANCE = new CourseFacade();
	private CourseService courseService;
	private ClassService classService;
	private PointService pointService;
	private CourseFacade() {
		courseService = new CourseService();
		classService = new ClassService();
		pointService = new PointService();
	}
	//course service
	@Override
	public CourseModel getCourseInfo(String courseId){
		return  courseService.getCourseInfo(courseId);
	}
	@Override
	public List<ClassModel> getLichTrinh(String courseId){
		return  courseService.getLichTrinh(courseId);
	}
	@Override
	public List<PointModel> getListPointOfStudent(String courseId){
		return  courseService.getListPointOfStudent(courseId);
	}
	//class service
	@Override
	public ClassModel getClassInfo(String classId){
		return  classService.getClassInfo(classId);
	}
	@Override
	public List<ClassModel> getClassOfCourse(String courseId){
		return classService.getClassOfCourse(courseId);
	}
	@Override
	public List<PointModel> getListStudentOfClass(String classId){
		return  classService.getListStudentOfClass(classId);
	}
	@Override
	public List<ClassModel> getTimeTableSV(String studentId){
		return  classService.getTimeTableGV(studentId);
	}
	@Override
	public List<ClassModel> getTimeTableGV(String teacherId){
		return classService.getTimeTableGV(teacherId);
	}
	@Override
	public boolean addStudentToClass(String studentId, String classId) {
		return classService.addStudentToClass(studentId, classId);
	}
	@Override
	public void NhapDiem(String studentId, String classId, PointDTO point){
		 classService.NhapDiem(studentId,classId,point);
	}
	@Override
	public void NhapDiemCaLop(String classId, List<PointDTO> listPoint){
		classService.NhapDiemCaLop(classId,listPoint);
	}
	//point service
	@Override
	public  PointModel getPoint(String studentID, String courseID){
		return	pointService.getPoint(studentID,courseID);
	}
	@Override
	public double getAveragePoint(String studentID, String courseID){
		return pointService.getAveragePoint(studentID,courseID);
	}
	@Override
	public  List<PointModel> getListClassOfStudent(String studentID){
		return pointService.getListClassOfStudent(studentID);
	}
	@Override
	public  List<PointModel> getListStudentOfCourse(String courseID){
		return pointService.getListStudentOfCourse(courseID);
	}//------------------------------------------------
	@Override
	public ClassModel findClassById(String classId) {
		return classService.findById(classId);
	}
}
