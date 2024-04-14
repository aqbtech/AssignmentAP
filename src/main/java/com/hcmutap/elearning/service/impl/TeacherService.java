package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.ClassDAO;
import com.hcmutap.elearning.dao.impl.TeacherDAO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("teacherService")
public class TeacherService implements ITeacherService {
	private final TeacherDAO teacherDAO;
	private final ClassDAO classDAO;
	private final CourseService courseService;
	private final ClassService classService;
	@Autowired
	public TeacherService(TeacherDAO teacherDAO, ClassDAO classDAO, CourseService courseService, ClassService classService) {
		this.teacherDAO = teacherDAO;
		this.classDAO = classDAO;
		this.courseService = courseService;
		this.classService = classService;
	}
	@Override
	public List<TeacherModel> findAll() {
		return teacherDAO.findAll();
	}
	@Override
	public List<TeacherModel> findBy(String key, String value) throws NotFoundException {
		List<TeacherModel> teacherModels = teacherDAO.findBy(key, value, Options.OptionBuilder.Builder().setEqual().build());
		if (teacherModels != null && !teacherModels.isEmpty()){
			return teacherModels;
		} else {
			throw new NotFoundException("Teacher not found");
		}
	}

	@Override
	public TeacherModel findById(String id) throws NotFoundException {
		TeacherModel teacherModel = teacherDAO.findById(id);
		if (teacherModel != null){
			return teacherModel;
		} else {
			throw new NotFoundException("Teacher not found");
		}
	}

	@Override
	public String save(TeacherModel teacherModel) {
		return teacherDAO.save(teacherModel);
	}

	@Override
	public void update(TeacherModel teacherModel) throws NotFoundException {
		TeacherModel teacherModels = teacherDAO.findById(teacherModel.getId());
		if (teacherModels == null){
			throw new NotFoundException("Teacher not found");
		}
		teacherDAO.update(teacherModel);
	}
	private void delete(String id) throws NotFoundException {
		TeacherModel teacherModel = teacherDAO.findById(id);
		if (teacherModel == null){
			throw new NotFoundException("Teacher not found id: " + id);
		}
		teacherDAO.delete(id);
	}
	@Override
	public void delete(List<String> ids) throws NotFoundException {
		for(String id : ids){
			delete(id);
		}
	}

	@Override
	public TeacherModel findByUsername(String username) throws NotFoundException {
		List<TeacherModel> teacherModels = teacherDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build());
		if (teacherModels != null && !teacherModels.isEmpty()){
			return teacherModels.getFirst();
		} else {
			throw new NotFoundException("Teacher with username " +username + "not found");
		}
	}

	@Override
	public boolean isExist(String id) {
		TeacherModel teacherModel =  teacherDAO.findById(id);
		return teacherModel != null;
	}

	@Override
	public List<ClassModel> getAllClass(String username) throws NotFoundException {
		List<ClassModel> classModels = new ArrayList<>();
		try {
			TeacherModel teacherModel = teacherDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
			if (teacherModel.getClasses() == null){
				return new ArrayList<>();
			} else {
				for(String classId : teacherModel.getClasses()){
					ClassModel classModel = CourseFacade.getInstance().findClassById(classId);
					classModels.add(classModel);
				}
			}
			return classModels;
		} catch (Exception e){
			throw new NotFoundException("Teacher not found");
		}
	}

	@Override
	public List<CourseModel> get_course(String teacherId){
		TeacherModel teacherModel = teacherDAO.findById(teacherId);
		List<CourseModel> result = new ArrayList<>();
		List<String> courses = teacherModel.getCourses();
		for (String e : courses){
			CourseModel c = CourseFacade.getInstance().getCourseInfo(e);
			result.add(c);
		}
		return result;
	}

	@Override
	public String Dangkilophoc(String teacherId, String classId) throws NotFoundException {
		ClassModel classModel = classDAO.getClassInfo(classId);
		TeacherModel teacherModel = teacherDAO.findById(teacherId);
		List<ClassModel> timetable = classDAO.getTimeTableGV(teacherId);

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
					return "Dang ki khong thanh cong v trung thoi gian";
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
		return "Dang ki thanh cong";
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

