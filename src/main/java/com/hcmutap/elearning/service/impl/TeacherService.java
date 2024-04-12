package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.AdminDAO;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.ClassDAO;
import com.hcmutap.elearning.dao.impl.TeacherDAO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.ITeacherService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("teacherService")
public class TeacherService implements ITeacherService {
	@Resource
	private TeacherDAO teacherDAO;
	@Resource
	private ClassDAO classDAO;
	@Resource
	private ClassService classService;
	@Resource
	private CourseService courseService;
	@Resource
	private AdminDAO<TeacherModel> basicDAO;
	@Override
	public List<TeacherModel> findAll() {
//		basicDAO.findAll();
		return teacherDAO.findAll();
	}

	@Override
	public List<TeacherModel> findBy(String key, String value) {
		return null;
	}

	@Override
	public TeacherModel findById(String id) {
		return teacherDAO.findById(id);
	}

	@Override
	public String save(TeacherModel teacherModel) {
		return teacherDAO.save(teacherModel);
	}

	@Override
	public void update(TeacherModel teacherModel) {
		teacherDAO.update(teacherModel);
	}

	@Override
	public void delete(List<String> ids) {
		for (String id : ids) {
			teacherDAO.delete(id);
		}
	}

	@Override
	public TeacherModel findByUsername(String username) {
		return teacherDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
	}

	@Override
	public boolean isExist(String id) {
		TeacherModel teacherModel =  teacherDAO.findById(id);
		return teacherModel != null;
	}

	@Override
	public List<ClassModel> getAllClass(String username) {
		List<ClassModel> classModels = new ArrayList<>();
		TeacherModel teacherModel = teacherDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
		if (teacherModel.getClasses() == null || teacherModel.getClasses().isEmpty()){
			return List.of();
		} else {
			for(String classId : teacherModel.getClasses()){
				ClassModel classModel = CourseFacade.getINSTANCE().findClassById(classId);
				classModels.add(classModel);
			}
		}
		return classModels;
	}

	@Override
	public List<CourseModel> get_course(String teacherId){
		TeacherModel teacherModel = teacherDAO.findById(teacherId);
		List<CourseModel> result = new ArrayList<>();
		List<String> courses = teacherModel.getCourses();
		for (String e : courses){
			CourseModel c = CourseFacade.getINSTANCE().getCourseInfo(e);
			result.add(c);
		}
		return result;
	}

	@Override
	public String Dangkilophoc(String teacherId, String classId){
		ClassModel classModel = classDAO.getClassInfo(classId);
		TeacherModel teacherModel = teacherDAO.findById(teacherId);
		List<ClassModel> timetable = classDAO.getTimeTableSV(teacherId);

		if (classModel.getTeacherId() != null) return "Da co giang vien dang ki";


		for (String e : teacherModel.getClasses()){
			if(e.equals(classId)){
				return "Dang day khoa hoc nay";
			}
		}

		for(ClassModel e : timetable){
			if(!e.getDayOfWeek().equals(classModel.getDayOfWeek())){
				continue;
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
					return "Unsuccessful";
				}
			}
		}
		CourseModel courseModel = courseService.getCourseInfo(classModel.getCourseId());
		teacherModel.getClasses().add(classId);
		teacherModel.getCourses().add(courseModel.getCourseId());
		classModel.setTeacherId(teacherModel.getId());
		classModel.setTeacherName(teacherModel.getFullName());
		classService.update(classModel);
		update(teacherModel);
		return "successful";
	}

//	@Override
//	public List<CourseModel> getCoursesModel(String teacherId, List<ClassModel> classes){
//		List<CourseModel> result = new ArrayList<>();
//		for (ClassModel e : classes){
//			CourseModel courseModel = courseService.getCourseInfo(e.getCourseId());
//			result.add(courseModel);
//		}
//		return result;
//	}
}

