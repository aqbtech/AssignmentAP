package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.Singleton;
import com.hcmutap.elearning.dto.RegisterDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.*;
import com.hcmutap.elearning.service.*;

import com.hcmutap.elearning.service.impl.RegisterService;
import com.hcmutap.elearning.service.impl.SemesterService;
import com.hcmutap.elearning.utils.MapperUtil;
import com.hcmutap.elearning.validator.RegisterDTOValidator;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "homeControllerOfAdmin")
public class HomeController {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Resource
	private IStudentService studentService;
	@Resource
	private ITeacherService teacherService;
	@Resource
	private IUserService userService;
	@Resource
	private IPointService pointService;
	private RegisterService registerService;
	@Autowired
	private RegisterDTOValidator registerDTOValidator;
	private ICourseService courseService;
	@Autowired
	private SemesterService semesterService;

	@Autowired
	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}
//	@Autowired
//	public void setRegisterDTOValidator(RegisterDTOValidator registerDTOValidator) {
//		this.registerDTOValidator = registerDTOValidator;
//	}
	@Autowired
	public void setRegisterService(RegisterService registerService) {
		this.registerService = registerService;
	}
	@GetMapping("/admin-home")
	public String index(Principal principal, ModelMap model) {
		UserModel userModel = null;
		try {
			userModel = userService.findByUsername(principal.getName());
			model.addAttribute("user", userModel);
			return "admin/views/home";
		} catch (NotFoundException e) {
			logger.error("User not found");
			return "redirect:/login";
		}

	}
	@GetMapping("/admin-management")
	public String viewAll(Model model, @RequestParam("type") String type,
						  @RequestParam(required = false) String keyword,
						  @RequestParam(defaultValue = "1") int page,
						  @RequestParam(defaultValue = "3") int size) {
		Page<?> pageResult = null;
		switch (type) {
			case "student":
				pageResult = studentService.getPage(keyword, page, size);
				model.addAttribute("updateLink", "admin-management/update");
				model.addAttribute("deleteLink", "admin-management/deleteAccount");
				model.addAttribute("href", "/admin-management?type=student");
				model.addAttribute("type", "student");
				break;
			case "teacher":
				pageResult = teacherService.getPage(keyword, page, size);
				model.addAttribute("updateLink", "admin-management/update");
				model.addAttribute("deleteLink", "admin-management/deleteAccount");
				model.addAttribute("href", "/admin-management?type=teacher");
				model.addAttribute("type", "teacher");
				break;
			case "course":
				pageResult = courseService.getPage(keyword, page, size);
				model.addAttribute("updateLink", "admin-management/update-course");
				model.addAttribute("deleteLink", "admin-management/deleteCourse");
				model.addAttribute("href", "/admin-management?type=course");
				model.addAttribute("type", "course");
				break;
			case "semester":
				pageResult = semesterService.getPage(keyword, page, size);
				model.addAttribute("updateLink", "admin-management/update-semester");
				model.addAttribute("deleteLink", "admin-management/delete-semester");
				model.addAttribute("href", "/admin-management?type=semester");
				model.addAttribute("type", "semester");
				break;
			default:
				logger.error("Type not found");
				return "redirect:/admin-home"; // TODO: redirect to error page
		}
		genePage(model, pageResult);
		return "admin/views/view-all-table";
	}

	private void genePage(Model model, Page<?> page) {
		model.addAttribute("models", page.getContent());
		model.addAttribute("currentPage", page.getNumber() + 1);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("pageSize", page.getSize());
	}

	@GetMapping("/admin-management/deleteAccount")
	public String deleteAccount(@RequestParam("id") String id,
								@RequestParam("type") String type,
								final RedirectAttributes redirectAttributes) {
		try {
			if (type.equals("teacher")) {
				TeacherModel teacher = teacherService.findById(id);
				if (teacher.getClasses().isEmpty()) {
					List<String> del = new ArrayList<>();
					del.add(teacher.getFirebaseId());
					teacherService.delete(del);
					userService.delete(teacher.getUsername());
					redirectAttributes.addFlashAttribute("message", "Xóa giáo viên " + id + " thành công!");
				} else {
					String message = "Giáo viên vẫn còn dạy các lớp";
					for (String classId : teacher.getClasses()) {
						message += " " + classId;
					}
					message += " nên chưa thể xóa!";
					redirectAttributes.addFlashAttribute("message", message);
				}
			} else {
				StudentModel student = studentService.findById(id);
				List<String> del = new ArrayList<>();
				del.add(student.getFirebaseId());
				studentService.delete(del);
				userService.delete(student.getUsername());
				List<PointModel> points = pointService.getListPointByStudentId(student.getId());
				List<String> delPoint = new ArrayList<>();
				for (PointModel point : points) {
					delPoint.add(point.getFirebaseId());
				}
				pointService.delete(delPoint);
				redirectAttributes.addFlashAttribute("message", "Xóa sinh viên " + student.getFullName() + " thành công!");
			}
			return "redirect:/admin-management?type=" + type;
		} catch (NotFoundException e) {
			logger.error("Can't delete account because not found");
			return "redirect:/404";
		}
	}

	@GetMapping("/admin-management/update")
	public String updateAccount(@RequestParam("id") String id,
								@RequestParam("type") String type) {
		return "redirect:/admin-management/view-info?id=" + id + "&type=" + type;
	}

	@PostMapping("/admin-management/view-info")
	public String updateAccount(@RequestParam("id") String id,
								@RequestParam("type") String type,
								@ModelAttribute("form") RegisterDTO form, ModelMap model) {
		try {
			if (type.equals("teacher")) {
				TeacherModel teacherModel = teacherService.findById(id);
				teacherModel.setFullName(form.getFullName());
				teacherModel.setAge(form.getAge());
				teacherModel.setAge(form.getAge());
				teacherModel.setDegree(form.getDegree());
				teacherService.update(teacherModel);
				model.addAttribute("type", "teacher");
				model.addAttribute("user", teacherModel);
			} else {
				StudentModel studentModel = studentService.findById(id);
				studentModel.setFullName(form.getFullName());
				studentModel.setAge(form.getAge());
				studentModel.setAge(form.getAge());
				studentModel.setMajor(form.getMajor());
				studentService.update(studentModel);
				model.addAttribute("type", "student");
				model.addAttribute("user", studentModel);
			}
			model.addAttribute("message", "Thông tin được chỉnh sửa thành công");
			return "admin/views/update-account";
		} catch (NotFoundException e) {
			// TODO: redirect to error page
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/admin-management/registration")
	public String regis_manage(ModelMap modelMap){
		Singleton check = Singleton.getInstance();
		boolean student_check = check.isStudent_state();
		boolean teacher_check = check.isTeacher_state();
		modelMap.addAttribute("student", student_check);
		modelMap.addAttribute("teacher", teacher_check);
		return "admin/views/manage_course_registration";
	}
	@PostMapping("/admin-management/registration")
	public String regis_manage(@RequestParam("teacher") String teacher,
							   @RequestParam("student") String student){
		Singleton check = Singleton.getInstance();
		if(teacher.equals("Close")){
			check.setTeacher_state(false);
		}
		if(teacher.equals("Open")){
			check.setTeacher_state(true);
		}
		if (student.equals("Close")){
			check.setStudent_state(false);
		}
		if (student.equals("Open")){
			check.setStudent_state(true);
		}
		return "redirect:/admin-management/registration";
	}

	@GetMapping("/admin-management/view-info")
	public String viewInfo(@RequestParam("id") String id,
						   @RequestParam("type") String type,
						   Model model) {
		try {
			if (type.equals("student")) {
				model.addAttribute("user", studentService.findById(id));
				model.addAttribute("type", "student");
			} else if (type.equals("teacher")) {
				model.addAttribute("type", "teacher");
				model.addAttribute("user", teacherService.findById(id));
			}
			model.addAttribute("form", new RegisterDTO());
			return "admin/views/update-account";
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@InitBinder("registerForm")
	protected void initBinder(WebDataBinder dataBinder) {
		dataBinder.addValidators(registerDTOValidator);
//		Object target = dataBinder.getTarget();
//		if (target == null) {
//			return;
//		}
//		System.out.println("Target=" + target);
//		if (target.getClass() == RegisterDTO.class) {
//			dataBinder.setValidator(registerDTOValidator);
//		}
	}

	@RequestMapping(value = "/admin-management/add-account", method = RequestMethod.GET)
	public String addAccount(Model model) {
		RegisterDTO form = new RegisterDTO();
		model.addAttribute("registerForm", form);
		return "admin/views/createAccount";
	}

	@RequestMapping(value = "/admin-management/add", method = RequestMethod.POST)
	public String addAccount(Model model,
							 @ModelAttribute("registerForm") @Validated RegisterDTO registerDTO,
							 BindingResult result,
							 final RedirectAttributes redirectAttributes) {
		try {
			if (result.hasErrors()) {
				model.addAttribute("message", "Failed to add account");
				return "admin/views/createAccount";
			}
			ModelMap modelMap = MapperUtil.getInstance().toModelMapFromDTO(registerDTO);
			String message = registerService.register(modelMap);
			if (message.equals("Success")) {
				return "redirect:/admin-management?type=" + registerDTO.getRole().toLowerCase();
			} else {
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/admin-management/add-account";
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
