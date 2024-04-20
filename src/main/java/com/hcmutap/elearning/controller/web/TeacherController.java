package com.hcmutap.elearning.controller.web;


import com.hcmutap.elearning.controller.admin.HomeController;
import com.hcmutap.elearning.Singleton;
import com.hcmutap.elearning.dto.Class_CourseDTO;
import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.dto.PointDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {
	private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);
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

	@GetMapping(value = "/course")
	public String teacher(@RequestParam("id") int id) {
		return "web/views/view_course_teacher";
	}

	@GetMapping(value = "/registration")
	public String regis(@RequestParam("courseId") String id, Principal principal, ModelMap model) {
		try {
			Singleton check = Singleton.getInstance();
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			TeacherModel teacherModel = teacherService.findById(infoDTO.getId());

			List<Class_CourseDTO> class_course = new ArrayList<>();
			List<Class_CourseDTO> class_course_of_teacher = class_courseService.getClass_Course(teacherModel.getUsername());
			if(!check.isTeacher_state()){
				model.addAttribute("message", "Hiện không được đăng ký");
				model.addAttribute("class_course_of_teacher", class_course_of_teacher);
				model.addAttribute("class_course", class_course);
				return "web/views/teacher-service/registration";
			}
			if(!class_courseService.checkClass_Course(class_course_of_teacher)){
				return "login/Rare_fault";
			}

			if(id.isEmpty()){
				model.addAttribute("class_course_of_teacher", class_course_of_teacher);
				model.addAttribute("class_course", class_course);
				return "web/views/teacher-service/registration";
			}
			if(courseService.isExist(id)){
				class_course = class_courseService.getByCourseId(id);
				model.addAttribute("class_course_of_teacher", class_course_of_teacher);
				model.addAttribute("class_course", class_course);
				return "web/views/teacher-service/registration";
			}
			model.addAttribute("message", "Khong co lop hoc ma ban tim kiem");
			model.addAttribute("class_course_of_teacher", class_course_of_teacher);
			model.addAttribute("class_course", class_course);
			return "web/views/teacher-service/registration";
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}

	}

	@PostMapping(value = "/registration")
	public String registed(@RequestParam("classId") String classId,Principal principal, ModelMap modelMap,
							final RedirectAttributes redirectAttributes){

		try {
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			TeacherModel teacherModel = teacherService.findById(infoDTO.getId());

			String message = teacherService.Dangkilophoc(teacherModel.getId(), classId);

			List<Class_CourseDTO> class_course = new ArrayList<>();
			List<Class_CourseDTO> class_course_of_teacher = class_courseService.getClass_Course(teacherModel.getUsername());

			redirectAttributes.addFlashAttribute("message", message);
			modelMap.addAttribute("class_course_of_teacher", class_course_of_teacher);
			modelMap.addAttribute("class_course", class_course);
			return "redirect:/teacher/registration?courseId=";
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
	@GetMapping(value = "/update_student")
	public String listStudent(@RequestParam("classId") String classId,
							  @RequestParam("courseId") String courseId,
							  final RedirectAttributes redirectAttributes,
							  Principal principal, ModelMap model){
		try {
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			TeacherModel teacherModel = teacherService.findById(infoDTO.getId());
			CourseModel courseModel = courseService.getCourseInfo(courseId);
			List<PointModel> pointModels = pointService.getListStudentByClassId(classId);
			model.addAttribute("points", pointModels);
			model.addAttribute("name","Danh sách lớp "+pointModels.getFirst().getClassName() +" - khóa học " +pointModels.getFirst().getCourseName());
			model.addAttribute("classId",classId);
			model.addAttribute("courseId",courseId);
			model.addAttribute("course",courseModel);
			return "web/views/teacher-service/InputScore/list_student";
		} catch (NotFoundException e) {
			logger.error("Error in update_student");
			return "redirect:/teacher/update_point";
		}
	}
	@PostMapping(value = "/update_student")
	public String listStudentt(@RequestParam("studentId") String studentId,
                               @RequestParam("courseId") String courseId,
                               @RequestParam("classId") String classId,
							   @ModelAttribute PointDTO pointDTO,
                               Principal principal, ModelMap model){
		try {
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			TeacherModel teacherModel = teacherService.findById(infoDTO.getId());
			CourseModel courseModel = courseService.getCourseInfo(courseId);
			classService.NhapDiem(studentId,classId,pointDTO);

			List<PointModel> pointModels = null;
			pointModels = pointService.getListStudentByClassId(classId);
			model.addAttribute("points", pointModels);
			model.addAttribute("name","Danh sách lớp "+pointModels.getFirst().getClassName() +" - khóa học " +pointModels.getFirst().getCourseName());
			model.addAttribute("classId",classId);
			model.addAttribute("courseId",courseId);
			model.addAttribute("course",courseModel);
			return "web/views/teacher-service/InputScore/list_student";
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
        }
	}

	@GetMapping(value = "/update_point")
	public String listStudent(ModelMap modelMap){
			modelMap.addAttribute("name","Hiện không thể tìm thấy sinh viên ");
			return "web/views/teacher-service/InputScore/UpdatePoint";
	}
}
