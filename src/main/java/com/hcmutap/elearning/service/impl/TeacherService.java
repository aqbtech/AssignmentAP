package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.ClassDAO;
import com.hcmutap.elearning.dao.impl.TeacherDAO;
import com.hcmutap.elearning.exception.CustomRuntimeException;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.exception.TransactionalException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.ITeacherService;
import com.hcmutap.elearning.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
		} catch (TransactionalException transactionalException) {
			throw new RuntimeException(transactionalException);
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
		try {
			TeacherModel teacherModels = teacherDAO.findById(teacherModel.getId());
			teacherDAO.update(teacherModel);
		} catch (TransactionalException transactionalException) {
			throw new CustomRuntimeException(transactionalException.getMessage(), "100");
		}
	}
	private void delete(String id) {
		try {
			TeacherModel teacherModel = teacherDAO.findById(id);
			teacherDAO.delete(id);
		} catch (TransactionalException transactionalException) {
			throw new CustomRuntimeException(transactionalException.getMessage(), "100");
		}
	}
	@Override
	public void delete(List<String> ids) {
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
			throw new NotFoundException("Teacher with username " + username + "not found");
		}
	}

	@Override
	public boolean isExist(String id) {
		try {
			TeacherModel teacherModel = teacherDAO.findById(id);
			return teacherModel != null;
		} catch (TransactionalException transactionalException) {
			throw new CustomRuntimeException(transactionalException.getMessage(), "404");
		}
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
		} catch (TransactionalException | NotFoundException transactionalException) {
			throw new RuntimeException(transactionalException);
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
					return "Bạn đang dạy lớp này";
				}
			}

			if (StringUtils.hasText(classModel.getTeacherId()))
				return "Lớp đã có giảng viên đăng ký";

			for(ClassModel e : timetable){
				if (TimeUtils.isSameTime(e, classModel)){
					return "Đăng ký không thành công vì trùng thời gian với " + e.getClassName();
				}
			}
			CourseModel courseModel = courseService.getCourseInfo(classModel.getCourseId());
			teacherModel.getClasses().add(classId);
			teacherModel.getCourses().add(courseModel.getCourseId());
			classModel.setTeacherId(teacherModel.getId());
			classModel.setTeacherName(teacherModel.getFullName());
			classService.update(classModel);
			update(teacherModel);
			return "Đăng ký thành công";
		} catch (TransactionalException transactionalException) {
			throw new RuntimeException(transactionalException);
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

