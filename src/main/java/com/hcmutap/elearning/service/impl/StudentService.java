package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.ClassDAO;
import com.hcmutap.elearning.dao.impl.CourseDAO;
import com.hcmutap.elearning.dao.impl.StudentDAO;
import com.hcmutap.elearning.dao.impl.PointDAO;
import com.hcmutap.elearning.exception.CustomRuntimeException;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.exception.TransactionalException;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.service.IClassService;
import com.hcmutap.elearning.service.ICourseService;
import com.hcmutap.elearning.service.IPointService;
import com.hcmutap.elearning.service.IStudentService;
import com.hcmutap.elearning.utils.TimeUtils;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
		try {
			studentDAO.update(studentModel);
		} catch (TransactionalException e) {
			throw new CustomRuntimeException(e.getMessage(), "100");
		}
	}

	@Override
	public void delete(List<String> ids) {
		ids.forEach(id -> {
			try {
				studentDAO.delete(id);
			} catch (TransactionalException e) {
				throw new CustomRuntimeException(e.getMessage(), "100");
			}
		});
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
			for(ClassModel e : timetable) {
				if (TimeUtils.isSameTime(e, classModel)){
					return "Đăng ký không thành công vì trùng thời gian với " + e.getClassName();
				}
			}
			for (String e : finished_course){
				if (e.equals(classModel.getCourseId())){
					return "Đăng ký không thành công vì bạn đã học qua môn này";
				}
			}

			return add_class_to_student(studentId, classID);
		} catch (TransactionalException transactionalException) {
			throw new RuntimeException(transactionalException);
		}
	}

	@Override
	public List<ClassModel> getTimetableById(String studentId) throws NotFoundException {
		StudentModel studentModel = null;
		try {
			studentModel = studentDAO.findById(studentId);
		} catch (TransactionalException transactionalException) {
			throw new RuntimeException(transactionalException);
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
		} catch (TransactionalException transactionalException) {
			throw new RuntimeException(transactionalException);
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
		} catch (TransactionalException transactionalException) {
			throw new RuntimeException(transactionalException);
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
		} catch (TransactionalException transactionalException) {
			throw new RuntimeException(transactionalException);
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
			boolean flag = classService.addStudentToClass(studentId,classId);
			if(flag) {
				return "Đăng ký thành công";
			}
			return "Đăng ký không thành công";
		} catch (TransactionalException transactionalException) {
			throw new RuntimeException(transactionalException);
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
		} catch (TransactionalException transactionalException) {
			throw new RuntimeException(transactionalException);
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

	@Override
	public Page<StudentModel> getPageByClassId(String key, String id, int page, int size) {
		try {
			List<PointModel> listTemp = pointService.getListStudentByClassId(id);
			List<StudentModel> listStudent = new ArrayList<>();
			for (PointModel tmp : listTemp) {
				listStudent.add(findById(tmp.getStudentId()));
			}

			if(key!=null) {
				String lowercaseKey = key.toLowerCase();
				listStudent = listStudent.stream()
						.filter(student -> student.getId().contains(lowercaseKey) ||
								student.getFullName().toLowerCase().contains(lowercaseKey))
						.collect(Collectors.toList());
			}

			long total = listStudent.size();

			int fromIndex = Math.min((page - 1) * size, listStudent.size());
			int toIndex = Math.min(fromIndex + size, listStudent.size());
			List<StudentModel> pageContent = listStudent.subList(fromIndex, toIndex);

			return new PageImpl<>(pageContent, PageRequest.of(page - 1, size), total);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}