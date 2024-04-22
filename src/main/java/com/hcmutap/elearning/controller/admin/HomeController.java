package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.Singleton;
import com.hcmutap.elearning.constant.SystemConstant;
import com.hcmutap.elearning.dto.RegisterDTO;
import com.hcmutap.elearning.exception.ConvertExcelToObjectException;
import com.hcmutap.elearning.exception.CustomRuntimeException;
import com.hcmutap.elearning.exception.MappingException;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.*;
import com.hcmutap.elearning.service.*;

import com.hcmutap.elearning.service.impl.RegisterService;
import com.hcmutap.elearning.service.impl.SemesterService;
import com.hcmutap.elearning.utils.MapperUtil;
import com.hcmutap.elearning.validator.RegisterDTOValidator;
import org.checkerframework.checker.units.qual.C;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller(value = "homeControllerOfAdmin")
public class HomeController {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private final IStudentService studentService;
	private final ITeacherService teacherService;
	private final IUserService userService;
	private final RegisterService registerService;
	private final RegisterDTOValidator registerDTOValidator;
	private final ICourseService courseService;
	private final SemesterService semesterService;
	@Autowired
	public HomeController( IStudentService studentService, ITeacherService teacherService,
						   IUserService userService, RegisterDTOValidator registerDTOValidator,
						   SemesterService semesterService, ICourseService courseService,
						   RegisterService registerService) {
		this.studentService = studentService;
		this.teacherService = teacherService;
		this.userService = userService;
		this.registerDTOValidator = registerDTOValidator;
		this.semesterService = semesterService;
		this.courseService = courseService;
		this.registerService = registerService;
	}
	@GetMapping("/admin-home")
	public String index(Principal principal, ModelMap model) {
		try {
			UserModel userModel = userService.findByUsername(principal.getName());
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
						  @RequestParam(defaultValue = "6") int size) {
		Page<?> pageResult;
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
				throw new CustomRuntimeException("Type not found", "404");
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
								RedirectAttributes model) {
		String message = registerService.deleteAccount(id, type);
		model.addFlashAttribute("message", message);
		return "redirect:/admin-management?type=" + type;
	}

	@GetMapping("/admin-management/update")
	public String updateAccount(@RequestParam("id") String id,
								@RequestParam("type") String type) {
		return "redirect:/admin-management/view-info?id=" + id + "&type=" + type;
	}

	@PostMapping("/admin-management/view-info")
	public String updateAccount(@RequestParam("id") String id,
								@RequestParam("type") String type,
								@ModelAttribute("form") RegisterDTO form,
								RedirectAttributes redirectAttributes) {
		form.setId(id);
		String message = registerService.updateAccount(form, type);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/admin-management/view-info?id=" + id + "&type=" + type;
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
			if (SystemConstant.ROLE_STUDENT.equalsIgnoreCase(type)) {
				model.addAttribute("user", studentService.findById(id));
				model.addAttribute("type", "student");
			} else if (SystemConstant.ROLE_TEACHER.equalsIgnoreCase(type)) {
				model.addAttribute("type", "teacher");
				model.addAttribute("user", teacherService.findById(id));
			}
			model.addAttribute("form", new RegisterDTO());
			return "admin/views/update-account";
		} catch (NotFoundException e) {
			throw new CustomRuntimeException(e.getMessage(), "404");
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
		if (result.hasErrors()) {
			model.addAttribute("message", "Failed to add account");
			return "admin/views/createAccount";
		}
		try {
			ModelMap modelMap = MapperUtil.getInstance().toModelMapFromDTO(registerDTO);
			String message = registerService.register(modelMap);
			if (message.equals("Success")) {
				return "redirect:/admin-management?type=" + registerDTO.getRole().toLowerCase();
			} else {
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/admin-management/add-account";
			}
		} catch (MappingException e) {
			logger.error("Can't add account because mapping error {}", e.getMessage());
			model.addAttribute("message", "Không thể thêm tài khoản do lỗi dữ liệu nhập vào");
			return "admin/views/createAccount";
		}
	}
	@GetMapping("/admin-management/add-by-file")
	public String addAccountByFile() {
		return "admin/views/add-many-account";
	}
	@PostMapping(value = "/admin-management/add-by-file")
	public String addAccount(@RequestParam("file") MultipartFile file, Model model) {
		try {
			Map<String, String> result = registerService.register(file);
			String message;
			List<String> listError = new ArrayList<>();
			if(!result.containsKey("Complete")) {
				message = "Failed to add account, message: " + result.get("Error");
			} else {
				message = "Complete: " + result.get("Complete") + " accounts\n";
				message += "Error: " + result.get("Error") + " accounts\n";
				result.forEach((key, value) -> {
					if (!key.equals("Complete") && !key.equals("Error")) {
						listError.add(key + ": " + value);
					}
				});
			}
			model.addAttribute("message", message);
			model.addAttribute("listError", listError);
			return "admin/views/add-many-account";
		} catch (ConvertExcelToObjectException | MappingException e) {
			logger.error("Cant add account because mapping error {}", e.getMessage());
			model.addAttribute("message", "Không thể thêm tài khoản do lỗi dữ liệu nhập vào");
			return "admin/views/add-many-account";
		}
	}
}
