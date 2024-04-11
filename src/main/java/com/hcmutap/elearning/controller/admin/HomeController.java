package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.dto.RegisterDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IStudentService;

import com.hcmutap.elearning.service.ITeacherService;
import com.hcmutap.elearning.service.IUserService;
import com.hcmutap.elearning.service.impl.RegisterService;
import com.hcmutap.elearning.utils.MapperUtil;
import com.hcmutap.elearning.validator.RegisterDTOValidator;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller(value = "homeControllerOfAdmin")
public class HomeController {
	@Resource
	private IStudentService studentService;
	@Resource
	private ITeacherService teacherService;
	@Resource
	private IUserService userService;
	private RegisterService registerService;
	private RegisterDTOValidator registerDTOValidator;
	@Autowired
	public HomeController(RegisterDTOValidator registerDTOValidator) {
		this.registerDTOValidator = registerDTOValidator;
	}
	@Autowired
	public void setRegisterService(RegisterService registerService) {
		this.registerService = registerService;
	}
	@GetMapping("/admin-home")
	public String index(Principal principal, ModelMap model) {
		UserModel userModel = null;
		try {
			userModel =  userService.findByUsername(principal.getName());
			model.addAttribute("user", userModel);
			return "admin/views/home";
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}

	}
	@GetMapping("/admin-management")
	public String viewAll(ModelMap model, @RequestParam("type") String type){
		if (type.equals("student")) {
			model.addAttribute("models", studentService.findAll());
			model.addAttribute("type", "student");
		}
		else if (type.equals("teacher")) {
			model.addAttribute("models", teacherService.findAll());
			model.addAttribute("type", "teacher");
		}
		return "admin/views/view-all-table";
	}
	@GetMapping("/admin-management/update")
	public String updateAccount(@RequestParam("id") String id,
								@RequestParam("type") String type) {
		return "redirect:/admin-management/view-info?id=" + id + "&type=" + type;
	}

	@GetMapping("/admin-management/view-info")
	public String viewInfo(@RequestParam("id") String id,
						   @RequestParam("type") String type,
						   Model model) {
		if (type.equals("student")){
			try {
				model.addAttribute("user", studentService.findById(id));
				model.addAttribute("type", "student");
			} catch (NotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		else if (type.equals("teacher")){
			try {
				model.addAttribute("user", teacherService.findById(id));
				model.addAttribute("type", "teacher");
			} catch (NotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return "admin/views/update-account";
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
		ModelMap modelMap = MapperUtil.getInstance().toModelMapFromDTO(registerDTO);
		String message = null;
		try {
			message = registerService.register(modelMap);
			if (message.equals("Success")){
				return "redirect:/admin-management?type=" + registerDTO.getRole().toLowerCase();
			} else {
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/admin-management/add-account";
			}
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
