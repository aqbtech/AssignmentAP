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
import com.hcmutap.elearning.service.IStudentService;
import com.hcmutap.elearning.service.IClassService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
//	@Resource
//	private IClassService classService;

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
	public boolean DangkiMonhoc(StudentModel studentModel, String classID) {
		ClassModel classModel = classDAO.getClassInfo(classID);
		// TODO: send message to course service, validate if student can register this course
		// is this class of course full? call class service to check
		// is this course conflict with other courses? check student's timetable
		// is this course have prerequisite courses? check student's finished courses
		// if all of these conditions are satisfied, then register this course
		// else return error message
		// add class to student's classes
		// call class service to add student to class
		List<CourseModel> finished_course = studentModel.getFinished_courses();
		List<ClassModel> timetable = classDAO.getTimeTableSV(studentModel.getStudentId());
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
		for (CourseModel e : finished_course){
			if (e.getCourseId().equals(classModel.getCourseId())){
				return false;
			}
		}


		if(!CourseFacade.getINSTANCE()
				.addStudentToClass(studentModel.getStudentId(), classModel.getClassId())){
			return false;
		}

		CourseModel course = courseDAO.findById(classModel.getCourseId());
		studentModel.getCourses().add(course);
		return true;
	}

	@Override
	public List<ClassModel> get_timetable(String studentId) {
		StudentModel studentModel = studentDAO.findById(studentId);
		return studentModel.getClasses();
	}
	@Override
	public List<CourseModel> Tientrinhhoctap(String studentId){
		StudentModel studentModel = studentDAO.findById(studentId);
//		List<ClassModel> classes = studentModel.getClasses();
//		for (ClassModel e : classes) {
//			PointService
//		}
		List<CourseModel> finished_courses = studentModel.getFinished_courses();
		return finished_courses;
	}
	@Override
	public List<PointModel> get_point(String studentId){
		StudentModel studentModel = studentDAO.findById(studentId);
		List<PointModel> point = null;
		point = pointDAO.findPoint(studentId);
		return point;
	}

	@Override
	public List<ClassModel> get_list_class_of_this_course(String courseId){
		return classDAO.getClassOfCourse(courseId);
	}

	@Override
	public boolean add_class_to_student() {
		return false;
	}

	@Override
	public List<ClassModel> getAllClass(String username) {
		List<ClassModel> classes = findByUsername(username).getClasses();
		return classes == null ? List.of() : classes;
	}

	public boolean isExist(String id) {
		return studentDAO.findById(id) != null;
	}
}