package com.hcmutap.elearning.controller.web;


import com.hcmutap.elearning.Singleton;
import com.hcmutap.elearning.dto.*;
import com.hcmutap.elearning.exception.ConvertExcelToObjectException;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.*;
import com.hcmutap.elearning.service.impl.Class_CourseService;
import com.hcmutap.elearning.service.impl.CourseService;
import com.hcmutap.elearning.service.impl.ExcelService;
import com.hcmutap.elearning.service.impl.UserService;
import com.hcmutap.elearning.utils.MapperUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

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
				model.addAttribute("message", "Hien khong duoc dang ky");
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

	private void genePage(Model model, Page<?> page) {
		model.addAttribute("points", page.getContent());
		model.addAttribute("currentPage", page.getNumber() + 1);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("pageSize", page.getSize());
	}


	@GetMapping(value = "/update_student")
	public String listStudent(@RequestParam("classId") String classId,
							  @RequestParam("courseId") String courseId,
							  @RequestParam(required = false) String keyword,
							  @RequestParam(defaultValue = "1") Integer page,
							  @RequestParam(defaultValue = "6") Integer size,
							  Principal principal, Model model){
		try{
			List<PointModel> check = pointService.getListStudentByClassId(classId);
		}catch (NotFoundException e){
			return "redirect:/teacher/update_point?classId="+classId+"&courseId="+courseId;
		}
		try{
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			if(infoDTO.getRole().equalsIgnoreCase("student")){
				model.addAttribute("message", "You are not a teacher");
				return "login/404_page";
			} else if (infoDTO.getRole().equalsIgnoreCase("teacher")) {
				if (!teacherService.isExistTeacherInClass(principal.getName(), classId)) {
					model.addAttribute("message", "You are not in this class");
					return "login/404_page";
				}
			} else {
				model.addAttribute("message", "You are not a teacher");
				return "login/404_page";
			}
			try {
				CourseModel courseModel = courseService.getCourseInfo(courseId);
				ClassModel cl = classService.findById(classId);

				Page<PointModel> pointPage = pointService.getPage(keyword,classId, page, size);
				genePage(model, pointPage);



				model.addAttribute("classId",classId);
				model.addAttribute("courseId",courseId);
				model.addAttribute("course",courseModel);
				model.addAttribute("name","Danh sách lớp "+ cl.getClassName() +" - khóa học " + courseModel.getCourseName());


				return "web/views/teacher-service/InputScore/list_student";
			} catch (NotFoundException e) {
				logger.error("Error in update_student");
				return "redirect:/teacher/update_point?classId="+classId+"&courseId="+courseId;
			}
		} catch(NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping(value = "/update_point")
	public String updatePoint(@RequestParam("studentId") String studentId,
							  @RequestParam("classId") String classId,
							  @RequestParam("courseId") String courseId,
							  @RequestParam(name="pointBT", defaultValue = "15") String pointBT,
							  @RequestParam(name="pointBTL", defaultValue = "15") String pointBTL,
							  @RequestParam(name="pointGK", defaultValue = "15") String pointGK,
							  @RequestParam(name="pointCK", defaultValue = "15") String pointCK) {
		try {
			PointDTO pointDTO = new PointDTO(studentId, Double.parseDouble(pointBT), Double.parseDouble(pointBTL),
					Double.parseDouble(pointGK), Double.parseDouble(pointCK));
			classService.NhapDiem(studentId, classId, pointDTO);
			return "redirect:/teacher/update_student?classId=" + classId + "&courseId=" + courseId;
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping(value = "/upload_Excel")

	public String uploadExcel(@RequestParam("classId") String classId,
							  @RequestParam("courseId") String courseId,
							  @ModelAttribute MultipartFile file, Model model) {
		try {
			ExcelService<PointExcelDTO> excelService = new ExcelService<>();
			Optional<List<PointExcelDTO>> list = excelService.readAndConvert(file, PointExcelDTO.class);

			List<PointModel> pointModels = pointService.getListStudentByClassId(classId);

			Map<String, String> complete = new HashMap<>();
			Map<String, String> failure = new HashMap<>();
			if (list.isPresent()) {
				for (PointExcelDTO pointExcelDTO : list.get()) {
					for(PointModel pointModel : pointModels){
						if(Objects.equals(pointModel.getStudentId(), pointExcelDTO.getStudentId())){
							PointDTO pointDTO = new PointDTO(pointModel.getStudentId(), pointExcelDTO.getPointBT(), pointExcelDTO.getPointBTL(),
									pointExcelDTO.getPointGK(), pointExcelDTO.getPointCK());
							if(classService.NhapDiem(pointModel.getStudentId(), classId, pointDTO)) {
								complete.put(pointModel.getStudentId(), pointModel.getStudentName());
							} else {
								failure.put(pointModel.getStudentId(), pointModel.getStudentName());
							}
						}
					}
				}
			} else {
				logger.atDebug().log("List is empty");
			}
			String result = "Upload complete" + complete.size() + " success, " + failure.size() + " failure";
			model.addAttribute("message", result);
			return "redirect:/teacher/update_student?classId=" + classId + "&courseId=" + courseId;
		} catch (ConvertExcelToObjectException e) {
			logger.error("Can't add account because {}", e.getMessage());
			model.addAttribute("message", e.getMessage());
			return "admin/views/add-many-account";
		} catch (NotFoundException e) {
			logger.error("Can't add account because NFE");
            throw new RuntimeException(e);
        }
    }


}
