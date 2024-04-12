package com.hcmutap.elearning.controller.web;

import com.hcmutap.elearning.dto.Class_CourseDTO;
import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.*;
import com.hcmutap.elearning.service.impl.Class_CourseService;
import com.hcmutap.elearning.service.impl.CourseService;
import com.hcmutap.elearning.service.impl.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {

	@Resource
	private UserService userService;
	@Resource
	private IStudentService studentService;
	@Resource
	private IClassService classService;
	@Resource
	private ITeacherService teacherService;
	@Resource
	private Class_CourseService class_courseService;
	@Resource
	private CourseService courseService;
	@Resource
	private ISemesterService semesterServicel;

	@Resource
	private IPointService pointService;
	@GetMapping(value = "/course")
	public String teacher(@RequestParam("id") int id) {
		return "web/views/view_course_teacher";
	}

	@GetMapping(value = "/registration")
	public String regis(@RequestParam("courseId") String id, Principal principal, ModelMap model){
		InfoDTO infoDTO = userService.getInfo(principal.getName());
		TeacherModel teacherModel = teacherService.findById(infoDTO.getId());

		List<Class_CourseDTO> class_course = new ArrayList<>();
		List<Class_CourseDTO> class_course_of_teacher = class_courseService.getClass_Course(teacherModel.getUsername());

		if(id == null){
			model.addAttribute("message", null);
			model.addAttribute("class_course_of_teacher", class_course_of_teacher);
			model.addAttribute("class_course", class_course);
			return "web/views/teacher-service/registration";
		}

		if(courseService.isExist(id)){
			class_course = class_courseService.getByCourseId(id);
			model.addAttribute("message", "Khoa hoc hien dang kha dung");
			model.addAttribute("class_course_of_teacher", class_course_of_teacher);
			model.addAttribute("class_course", class_course);
			return "web/views/teacher-service/registration";
		}
		model.addAttribute("message", "Lop hoc khong kha dung");
		model.addAttribute("class_course_of_teacher", class_course_of_teacher);
		model.addAttribute("class_course", class_course);
		return "web/views/teacher-service/registration";
	}

	@PostMapping(value = "/registration")
	public String registed(@RequestParam("classId") String classId,Principal principal, ModelMap modelMap){
		InfoDTO infoDTO = userService.getInfo(principal.getName());
		TeacherModel teacherModel = teacherService.findById(infoDTO.getId());

		String message = teacherService.Dangkilophoc(teacherModel.getId(), classId);

		List<Class_CourseDTO> class_course = new ArrayList<>();
		List<Class_CourseDTO> class_course_of_teacher = class_courseService.getClass_Course(teacherModel.getUsername());


		modelMap.addAttribute("message", message);
		modelMap.addAttribute("class_course_of_teacher", class_course_of_teacher);
		modelMap.addAttribute("class_course", class_course);
		return "web/views/teacher-service/registration";
	}

	@GetMapping(value = "/timetable")
	public String timetable(Principal principal,ModelMap model){
		InfoDTO infoDTO = userService.getInfo(principal.getName());
		TeacherModel teacherModel = teacherService.findById(infoDTO.getId());
		List<ClassModel> classes = teacherService.getAllClass(teacherModel.getUsername());
		model.addAttribute("classes", classes);
		return "web/views/teacher-service/time-table";
	}

}
