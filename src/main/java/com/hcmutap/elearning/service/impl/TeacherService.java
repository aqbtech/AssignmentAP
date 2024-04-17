package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.ClassDAO;
import com.hcmutap.elearning.dao.impl.TeacherDAO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.exception.NotFoundInDB;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
		TeacherModel teacherModel = null;
		try {
			teacherModel = teacherDAO.findById(id);
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
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
		TeacherModel teacherModels = null;
		try {
			teacherModels = teacherDAO.findById(teacherModel.getId());
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
		if (teacherModels == null){
			throw new NotFoundException("Teacher not found");
		}
		teacherDAO.update(teacherModel);
	}
	private void delete(String id) throws NotFoundException {
		TeacherModel teacherModel = null;
		try {
			teacherModel = teacherDAO.findById(id);
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
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
		TeacherModel teacherModel = null;
		try {
			teacherModel = teacherDAO.findById(id);
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
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
					ClassModel classModel = classService.findById(classId);
					classModels.add(classModel);
				}
			}
			return classModels;
		} catch (Exception e){
			throw new NotFoundException("Teacher not found");
		}
	}

	@Override
	public List<CourseModel> get_course(String teacherId) {
		try {
		TeacherModel teacherModel = teacherDAO.findById(teacherId);
		List<CourseModel> result = new ArrayList<>();
		List<String> courses = teacherModel.getCourses();
		for (String e : courses){
			CourseModel c = courseService.getCourseInfo(e);
			result.add(c);
		}
		return result;
		} catch (NotFoundInDB | NotFoundException notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
	}

	@Override
	public Page<TeacherModel> getPage(String keyword, int page, int limit) {
		return teacherDAO.search(keyword, PageRequest.of(page - 1, limit));
	}

	@Override
	public Page<TeacherModel> getPage(int page, int limit) {
		return teacherDAO.findAll(PageRequest.of(page - 1, limit));
	}

	@Override
	public String Dangkilophoc(String teacherId, String classId) throws NotFoundException {
		try {
			ClassModel classModel = classDAO.getClassInfo(classId);
			TeacherModel teacherModel = teacherDAO.findById(teacherId);
			List<ClassModel> timetable = classDAO.getTimeTableGV(teacherId);

			for (String e : teacherModel.getClasses()){
				if(e.equals(classId)){
					return "Dang day lop hoc nay";
				}
			}

			if (classModel.getTeacherId() != null) return "Da co giang vien dang ki";

			for(ClassModel e : timetable){
				if(!e.getDayOfWeek().equals(classModel.getDayOfWeek())){
					continue;
				}
				else {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
					LocalTime time_start = LocalTime.parse(e.getTimeStart(), formatter);
					LocalTime time_end = LocalTime.parse(e.getTimeEnd(), formatter);
					LocalTime time_start_new_class = LocalTime.parse(classModel.getTimeStart(), formatter);
					LocalTime time_end_new_class = LocalTime.parse(classModel.getTimeEnd(), formatter);
					if(time_end.isBefore(time_start_new_class)){
						break;
					} else if (time_end_new_class.isBefore(time_start)) {
						break;
					}
					else {
						return "Dang ky khong thanh cong vi trung thoi gian voi " + e.getClassName();
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
			return "Dang ky thanh cong";
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
	}

	@Override
	public boolean isExistTeacherInClass(String username, String classId) throws NotFoundException {
		List<ClassModel> cl = getAllClass(username);
		boolean check = false;
		for (ClassModel classModel1 : cl) {
			if (classModel1.getClassId().equals(classId)) {
				check = true;
				break;
			}
		}
		return check;
	}
}

