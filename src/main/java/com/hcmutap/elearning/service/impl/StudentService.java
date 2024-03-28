package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.CourseDAO;
import com.hcmutap.elearning.dao.StudentDAO;
import com.hcmutap.elearning.dao.PointDAO;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.service.IStudentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements IStudentService {
//	@Autowired
//	private StudentRepository studentRepository;
	@Resource
	private StudentDAO studentDAO;
	@Resource
	private CourseDAO courseDAO;
	@Resource
	private  PointDAO pointDAO;
	@Override
	public List<StudentModel> findAll() {
		return studentDAO.findAll();
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
	public void delete(String id) {
		studentDAO.delete(id);
	}
	@Override
	public StudentModel findById(String id){
		return studentDAO.findById(id);
	}
	@Override
	public void DangkiMonhoc(StudentModel studentModel, String courseID) {
		CourseModel course = courseDAO.findById(courseID);
		studentModel.getCourses().add(course);
	}
	@Override
	public List<ClassModel> get_timetable(String studentId) {
		StudentModel studentModel = studentDAO.findById(studentId);
		return studentModel.getClasses();
	}
	@Override
	public List<CourseModel> Tientrinhhoctap(String studentId){
		StudentModel studentModel = studentDAO.findById(studentId);
		List<ClassModel> classes = studentModel.getClasses();
        for (ClassModel e : classes) {
            if (e.getTimeEnd().equals(" ")) {
//				if(point of this course is greater than or equal to 4)
//				update finished_course by adding this course to list<CourseModel> finished_courses
//				if(this course exist in  finished_courses and course's point greater than) then update
				int x = 0;
            }
        }
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
//	@Override
//	public String getScore(StudentModel studentModel) {
//
//	}
//	@Override
//	public void search_AccumulatedCredits(StudentModel studentModel){
//
//	}
}
