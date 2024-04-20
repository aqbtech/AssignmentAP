package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.ClassDAO;
import com.hcmutap.elearning.dao.impl.CourseDAO;
import com.hcmutap.elearning.dao.impl.StudentDAO;
import com.hcmutap.elearning.dao.impl.PointDAO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.service.IClassService;
import com.hcmutap.elearning.service.ICourseService;
import com.hcmutap.elearning.service.IPointService;
import com.hcmutap.elearning.service.IStudentService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
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
	@Resource
	private IClassService classService;
	@Resource
	private ICourseService courseService;
	@Override
	public List<StudentModel> findAll() {
		return studentDAO.findAll();
	}

	@Override
	public List<StudentModel> findBy(String key, String value) throws NotFoundException {
		List<StudentModel> studentModels = studentDAO.findBy(key, value, Options.OptionBuilder.Builder().setEqual().build());
		if (studentModels.isEmpty()) {
			throw new NotFoundException("Student not found");
		}
		return studentModels;
	}

	@Override
	public String save(StudentModel studentModel) {
		return studentDAO.save(studentModel);
	}
	@Override
	public void update(StudentModel studentModel) throws NotFoundException {
		if (!isExist(studentModel.getId())) {
			throw new NotFoundException("Student not found");
		}
		studentDAO.update(studentModel);
	}

	@Override
	public void delete(List<String> ids) {
		ids.forEach(id -> studentDAO.delete(id));
	}

	@Override
	public StudentModel findByUsername(String username) throws NotFoundException {
		List<StudentModel> studentModels = studentDAO.findBy("username", username, Options.OptionBuilder.Builder().setEqual().build());
		if (studentModels.isEmpty()) {
			throw new NotFoundException("Student not found");
		}
		return studentModels.getFirst();
	}

	@Override
	public StudentModel findById(String id) throws NotFoundException {
		try {
			return studentDAO.findById(id);
		} catch (Exception e) {
			throw new NotFoundException("Student not found in StudentService.findById");
		}
	}
	@Override
	public String DangkiMonhoc(String studentId, String classID) throws NotFoundException {
		try {
			ClassModel classModel = classDAO.getClassInfo(classID);
			if(courseDAO.findById(classModel.getCourseId()) == null){
				return "Không tìm thấy khóa học";
			};
			StudentModel studentModel = studentDAO.findById(studentId);

			List<String> finished_course = studentModel.getFinished_courses();
			List<ClassModel> timetable = classDAO.getTimeTableSV(studentModel.getId());
			for (String e : studentModel.getCourses()){
				if (e.equals(classModel.getCourseId())){
					return "Đăng ký không thành công vì bạn đang đăng ký môn này";
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
					LocalTime time_start_new_class = LocalTime.parse(classModel.getTimeStart(), formatter);
					LocalTime time_end_new_class = LocalTime.parse(classModel.getTimeEnd(), formatter);
					if(time_end.isBefore(time_start_new_class)){
						break;
					} else if (time_end_new_class.isBefore(time_start)) {
						break;
					}
					else {
						return "Đăng ký không thànhh công vì trùng thời gian với " + e.getClassName();
					}
				}
			}
			for (String e : finished_course){
				if (e.equals(classModel.getCourseId())){
					return "Đăng ký không thành công vì bạn đã học qua môn này";
				}
			}

			return add_class_to_student(studentId, classID);
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
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
	public List<ClassModel> getTimetableById(String studentId) throws NotFoundException {
		StudentModel studentModel = null;
		try {
			studentModel = studentDAO.findById(studentId);
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
		List<String> classes = studentModel.getClasses();
		List<ClassModel> result = new ArrayList<>();
		for(String e : classes){
			ClassModel c = classService.getClassInfo(e);
			result.add(c);
		}

		return result;
	}
	@Override
	public List<PointModel> LearningProcess(String studentId) throws NotFoundException {
		StudentModel studentModel = null;
		try {
			studentModel = studentDAO.findById(studentId);
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
		List<String> finished_courses = studentModel.getFinished_courses();
		List<PointModel> result = new ArrayList<>();
		for(String e : finished_courses){
			CourseModel c = courseService.getCourseInfo(e);
			result.add(pointService.getPoint(studentId, c.getCourseId()));
		}
		return result;
	}
	@Override
	public List<PointModel> getPointById(String studentId) throws NotFoundException {
		try {
			StudentModel studentModel = studentDAO.findById(studentId);
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
		List<PointModel> point = new ArrayList<>();
		point = pointDAO.findPoint(studentId);
		return point;
	}

	@Override
	public List<CourseModel> getCourseByIf(String studentId) throws NotFoundException {
		StudentModel studentModel = null;
		try {
			studentModel = studentDAO.findById(studentId);
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
		List<CourseModel> result = new ArrayList<>();
		List<String> courses = studentModel.getCourses();
		for (String e : courses){
			CourseModel c = courseService.getCourseInfo(e);
			result.add(c);
		}
		return result;
	}

	@Override
	public List<ClassModel> getListClassByCourseId(String courseId){
		return classDAO.getClassOfCourse(courseId);
	}

	@Override
	public String add_class_to_student(String studentId, String classId) throws NotFoundException {
		try {
			StudentModel studentModel = studentDAO.findById(studentId);
			ClassModel classModel = classDAO.getClassInfo(classId);
			CourseModel courseModel = courseDAO.findById(classModel.getCourseId());
			studentModel.getCourses().add(courseModel.getCourseId());
			studentModel.getClasses().add(classModel.getClassId());
			update(studentModel);
			classService.addStudentToClass(studentId,classId);
			return "Đăng ký thành công";
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
	}

	@Override
	public List<ClassModel> getAllClass(String username) throws NotFoundException {
		List<String> classes = findByUsername(username).getClasses();
		List<ClassModel> result = new ArrayList<>();
		for (String e : classes){
			ClassModel c = classService.getClassInfo(e);
			result.add(c);
		}
		return result;
	}
	@Override
	public boolean isExist(String id) {
		try {
			return studentDAO.findById(id) != null;
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
	}
	@Override
	public boolean isExistStudentInClass(String username, String classId) throws NotFoundException {
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

	@Override
	public Page<StudentModel> getPage(String keyword, int page, int limit) {
		return studentDAO.search(keyword, PageRequest.of(page - 1, limit));
	}

	@Override
	public Page<StudentModel> getPage(int page, int limit) {
		return studentDAO.findAll(PageRequest.of(page - 1, limit));
	}
}