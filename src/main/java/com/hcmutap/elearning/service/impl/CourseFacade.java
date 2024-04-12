package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.impl.*;
import com.hcmutap.elearning.dto.PointDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.ICourseFacade;

import java.util.List;

public class CourseFacade implements ICourseFacade {
	private static CourseFacade INSTANCE;
	private CourseService courseService;
	private ClassService classService;
	private PointService pointService;

	public static CourseFacade getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CourseFacade();
		}
		return INSTANCE;
	}
	private CourseFacade() {
		courseService = new CourseService();
		classService = new ClassService(new CourseDAO(), new StudentDAO(), new PointDAO(), new ClassDAO(), new TeacherDAO());
		pointService = new PointService();
	}
	//course service
	@Override
	public CourseModel getCourseInfo(String courseId){
		return  courseService.getCourseInfo(courseId);
	}
	@Override
	public List<ClassModel> getLichTrinh(String courseId) throws NotFoundException {
		return  courseService.getLichTrinh(courseId);
	}
	@Override
	public List<PointModel> getListPointOfStudent(String courseId) throws NotFoundException {
		return  courseService.getListPointOfStudent(courseId);
	}
	//class service
	@Override
	public ClassModel getClassInfo(String classId) throws NotFoundException {
		return  classService.getClassInfo(classId);
	}
	@Override
	public List<ClassModel> getClassOfCourse(String courseId){
		return classService.getClassOfCourse(courseId);
	}
	@Override
	public List<PointModel> getListStudentOfClass(String classId) throws NotFoundException {
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
	public boolean addStudentToClass(String studentId, String classId) throws NotFoundException {
		return classService.addStudentToClass(studentId, classId);
	}
	@Override
	public boolean NhapDiem(String studentId, String classId, PointDTO point) throws NotFoundException {
		 return classService.NhapDiem(studentId,classId,point);
	}
	@Override
	public void NhapDiemCaLop(String classId, List<PointDTO> listPoint) throws NotFoundException {
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
	public  List<PointModel> getListStudentOfCourse(String courseID) throws NotFoundException {
		return pointService.getListStudentByCourseId(courseID);
	}//------------------------------------------------
	@Override
	public ClassModel findClassById(String classId) throws NotFoundException {
		return classService.findById(classId);
	}
}
