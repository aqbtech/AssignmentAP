package com.hcmutap.elearning.controller.web;

import com.hcmutap.elearning.dao.impl.PointDAO;
import com.hcmutap.elearning.dto.Class_CourseDTO;
import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.*;
import com.hcmutap.elearning.service.impl.Class_CourseService;
import com.hcmutap.elearning.service.impl.CourseService;
import com.hcmutap.elearning.service.impl.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	private IPointService pointService;
	@Resource
	private Class_CourseService class_courseService;
	@Resource
	private CourseService courseService;
	@Resource
	private ISemesterService semesterServicel;

	@GetMapping(value = "/course")
	public String teacher(@RequestParam("id") int id) {
		return "web/views/view_course_teacher";
	}

	@GetMapping(value = "/registration")
	public String regis(@RequestParam("courseId") String id, Principal principal, ModelMap model) {
		try {
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
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}

	}

	@PostMapping(value = "/registration")
	public String registed(@RequestParam("classId") String classId,Principal principal, ModelMap modelMap){

		try {
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			TeacherModel teacherModel = teacherService.findById(infoDTO.getId());

			String message = teacherService.Dangkilophoc(teacherModel.getId(), classId);

			List<Class_CourseDTO> class_course = new ArrayList<>();
			List<Class_CourseDTO> class_course_of_teacher = class_courseService.getClass_Course(teacherModel.getUsername());


			modelMap.addAttribute("message", message);
			modelMap.addAttribute("class_course_of_teacher", class_course_of_teacher);
			modelMap.addAttribute("class_course", class_course);
			return "web/views/teacher-service/registration";
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}

	}

	@GetMapping(value = "/timetable")
	public String timetable(Principal principal,ModelMap model){
		try {
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			TeacherModel teacherModel = teacherService.findById(infoDTO.getId());
			List<ClassModel> classes = teacherService.getAllClass(teacherModel.getUsername());
			model.addAttribute("classes", classes);
			return "web/views/teacher-service/time-table";
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping(value = "/inputscore")
	public String inputscore(Principal principal,ModelMap model){
		try {
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			TeacherModel teacherModel = teacherService.findById(infoDTO.getId());
			List<ClassModel> classes = teacherService.getAllClass(teacherModel.getUsername());
			model.addAttribute("classes", classes);
			return "web/views/teacher-service/InputScore/inputscore";
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	@GetMapping(value = "/liststudent")
	public String listStudent(@RequestParam("classId") String classId, Principal principal, ModelMap model){
		try {
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			TeacherModel teacherModel = teacherService.findById(infoDTO.getId());

			List<PointModel> pointModels = pointService.getListStudentByClassId(classId);
			model.addAttribute("points", pointModels);
			model.addAttribute("name1",pointModels.getFirst().getClassName() +" - khóa học " +pointModels.getFirst().getCourseName());

			return "web/views/teacher-service/InputScore/list_student";
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	@PostMapping(value = "/liststudent")
	public String listStuDenTed(@RequestParam("classId") String classId, Principal principal, ModelMap model){
		try {
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			TeacherModel teacherModel = teacherService.findById(infoDTO.getId());

			List<PointModel> pointModels = pointService.getListStudentByClassId(classId);
			model.addAttribute("points", pointModels);
			model.addAttribute("name1",pointModels.getFirst().getClassName() +" - khóa học " +pointModels.getFirst().getCourseName());

			return "web/views/teacher-service/InputScore/list_student";

		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}


}
