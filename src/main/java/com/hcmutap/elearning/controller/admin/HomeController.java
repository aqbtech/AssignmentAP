package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.dto.RegisterDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IPointService;
import com.hcmutap.elearning.service.IStudentService;

import com.hcmutap.elearning.service.ITeacherService;
import com.hcmutap.elearning.service.IUserService;
import com.hcmutap.elearning.service.impl.RegisterService;
import com.hcmutap.elearning.utils.MapperUtil;
import com.hcmutap.elearning.validator.RegisterDTOValidator;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
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
import java.util.List;

@Controller(value = "homeControllerOfAdmin")
public class HomeController {
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
			throw new RuntimeException(e);
		}

	}
	@GetMapping("/admin-management")
	public String viewAll(ModelMap model, @RequestParam("type") String type,
						  @RequestParam(required = false) String keyword,
						  @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
		if (type.equals("student")) {
			Page<StudentModel> studentPage = studentService.getPage(keyword, page, size);
			List<StudentModel> studentModels = studentPage.getContent();
			model.addAttribute("models", studentModels);
			model.addAttribute("currentPage", studentPage.getNumber() + 1);
			model.addAttribute("totalItems", studentPage.getTotalElements());
			model.addAttribute("totalPages", studentPage.getTotalPages());
			model.addAttribute("pageSize", studentPage.getSize());
//			model.addAttribute("models", studentService.findAll());
			model.addAttribute("type", "student");
		}
		else if (type.equals("teacher")) {
			Page<TeacherModel> teacherPage = teacherService.getPage(keyword, page, size);
			List<TeacherModel> teacherModels = teacherPage.getContent();
			model.addAttribute("models", teacherModels);
			model.addAttribute("currentPage", teacherPage.getNumber() + 1);
			model.addAttribute("totalItems", teacherPage.getTotalElements());
			model.addAttribute("totalPages", teacherPage.getTotalPages());
			model.addAttribute("pageSize", teacherPage.getSize());
//			model.addAttribute("models", teacherService.findAll());
			model.addAttribute("type", "teacher");
		}
		return "admin/views/view-all-table";
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
				redirectAttributes.addFlashAttribute("message", "Xóa sinh viên " + id + " thành công!");
			}
			return "redirect:/admin-management?type=" + type;
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
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
