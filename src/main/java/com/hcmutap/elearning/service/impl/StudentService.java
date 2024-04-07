package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.ClassDAO;
import com.hcmutap.elearning.dao.impl.CourseDAO;
import com.hcmutap.elearning.dao.impl.StudentDAO;
import com.hcmutap.elearning.dao.impl.PointDAO;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.service.IPointService;
import com.hcmutap.elearning.service.IStudentService;
import com.hcmutap.elearning.service.IClassService;
import groovy.transform.Undefined;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class StudentService implements IStudentService {
	@Resource
	private StudentDAO studentDAO;
	@Resource
	private CourseDAO courseDAO;
	@Resource
	private ClassDAO classDAO;
	@Resource
	private  PointDAO pointDAO;
	@Resource
	private IPointService pointService;

	public boolean checkStudentId(String studentId){
		return studentId.length() < 8;
	}
	@Override
	public List<StudentModel> findAll() {
		return studentDAO.findAll();
	}

	@Override
	public List<StudentModel> findBy(String key, String value) {
		return studentDAO.findBy(key, value, Options.OptionBuilder.Builder().setEqual().build());
	}

	@Override
	public String save(StudentModel studentModel) {
		return studentDAO.save(studentModel);
	}
	@Override
	public void update(StudentModel studentModel) {
		studentDAO.update(studentModel);
	}

	@Override
	public void delete(List<String> ids) {
		ids.forEach(id -> studentDAO.delete(id));
	}

	@Override
	public StudentModel findByUsername(String username) {
		return studentDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
	}

	@Override
	public StudentModel findById(String id){
		return studentDAO.findById(id);
	}
	@Override
	public boolean DangkiMonhoc(String studentId, String classID) {
		if(!checkStudentId(studentId)) return false;
		ClassModel classModel = classDAO.getClassInfo(classID);
		StudentModel studentModel = studentDAO.findById(studentId);
		// TODO: send message to course service, validate if student can register this course
		// is this class of course full? call class service to check
		// is this course conflict with other courses? check student's timetable
		// is this course have prerequisite courses? check student's finished courses
		// if all of these conditions are satisfied, then register this course
		// else return error message
		// add class to student's classes
		// call class service to add student to class
		List<String> finished_course = studentModel.getFinished_courses();
		List<ClassModel> timetable = classDAO.getTimeTableSV(studentModel.getId());
		for(ClassModel e : timetable){
			if(!e.getDayOfWeek().equals(classModel.getDayOfWeek())){
				break;
			}
			else {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
				LocalTime time_start = LocalTime.parse(e.getTimeStart(), formatter);
				LocalTime time_end = LocalTime.parse(e.getTimeEnd(), formatter);
				LocalTime time_start_new_class = LocalTime.parse(e.getTimeStart(), formatter);
				LocalTime time_end_new_class = LocalTime.parse(e.getTimeEnd(), formatter);
				if(time_end.isBefore(time_start_new_class)){
					break;
				} else if (time_end_new_class.isBefore(time_start)) {
					break;
				}
				else {
					return false;
				}
			}
		}
		for (String e : finished_course){
			if (e.equals(classModel.getCourseId())){
				return false;
			}
		}


		if(!CourseFacade.getINSTANCE()
				.addStudentToClass(studentModel.getId(), classModel.getClassId())){
			return false;
		}

		return true;
	}

	public static Comparator<ClassModel> getDateTimeComparator() {
		return new Comparator<ClassModel>() {
			@Override
			public int compare(ClassModel entry1, ClassModel entry2) {

				DayOfWeek day1 = DayOfWeek.valueOf(entry1.getDayOfWeek().toUpperCase());
				DayOfWeek day2 = DayOfWeek.valueOf(entry2.getDayOfWeek().toUpperCase());

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
				LocalTime time_start_1 = LocalTime.parse(entry1.getTimeStart(), formatter);
				LocalTime time_start_2 = LocalTime.parse(entry2.getTimeStart(), formatter);

				int dateComparison = day1.compareTo(day2);
				if (!day1.equals(day2)) {
					return dateComparison;
				}

				return time_start_1.compareTo(time_start_2);
			}
		};
	}

	@Override
	public List<ClassModel> get_timetable(String studentId) {
		if (!checkStudentId(studentId)) return List.of();
		StudentModel studentModel = studentDAO.findById(studentId);
		List<String> classes = studentModel.getClasses();
		List<ClassModel> result = null;
		for(String e : classes){
			ClassModel c = CourseFacade.getINSTANCE().getClassInfo(e);
			result.add(c);
		}

//		classes.sort(getDateTimeComparator());

		return result == null ? List.of() : result;
	}
	@Override
	public List<PointModel> Tientrinhhoctap(String studentId){
		if (!checkStudentId(studentId)) return List.of();
		StudentModel studentModel = studentDAO.findById(studentId);
		List<String> finished_courses = studentModel.getFinished_courses();
		List<PointModel> result = null;
		for(String e : finished_courses){
			CourseModel c = CourseFacade.getINSTANCE().getCourseInfo(e);
			result.add(pointService.getPoint(studentId, c.getCourseId()));
		}
		return result;
	}
	@Override
	public List<PointModel> get_point(String studentId){
		if (!checkStudentId(studentId)) return List.of();
		StudentModel studentModel = studentDAO.findById(studentId);
		List<PointModel> point = null;
		point = pointDAO.findPoint(studentId);
		return point;
	}

	@Override
	public List<CourseModel> get_course(String studentId){
		if (!checkStudentId(studentId)) return List.of();
		StudentModel studentModel = studentDAO.findById(studentId);
		List<CourseModel> result = null;
		List<String> courses = studentModel.getCourses();
		for (String e : courses){
			CourseModel c = CourseFacade.getINSTANCE().getCourseInfo(e);
			result.add(c);
		}
		return result == null ? List.of() : result;
	}

	@Override
	public List<ClassModel> get_list_class_of_this_course(String courseId){

		return classDAO.getClassOfCourse(courseId);
	}

	@Override
	public boolean add_class_to_student(String studentId, String classId) {
		if (!checkStudentId(studentId)) return false;
		if(this.DangkiMonhoc(studentId, classId)){
			StudentModel studentModel = studentDAO.findById(studentId);
			ClassModel classModel = classDAO.getClassInfo(classId);
			CourseModel courseModel = courseDAO.findById(classModel.getCourseId());
			studentModel.getCourses().add(courseModel.getCourseId());
			studentModel.getClasses().add(classModel.getClassId());
			return true;
		}
		return false;
	}

	@Override
	public List<ClassModel> getAllClass(String username) {
		List<String> classes = findByUsername(username).getClasses();
		List<ClassModel> result = null;
		for (String e : classes){
			ClassModel c = CourseFacade.getINSTANCE().getClassInfo(e);
			result.add(c);
		}
		return result == null ? List.of() : result;
	}

	public boolean isExist(String id) {
		return studentDAO.findById(id) != null;
	}
}