package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.dto.RegisterDTO;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IStudentService;

import com.hcmutap.elearning.service.ITeacherService;
import com.hcmutap.elearning.service.IUserService;
import com.hcmutap.elearning.utils.MapperUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller(value = "homeControllerOfAdmin")
public class HomeController {
	@Resource
	private IStudentService studentService;
	@Resource
	private ITeacherService teacherService;
	@Resource
	private IUserService userService;
	@GetMapping("/admin-home")
	public String index(Principal principal, ModelMap model) {
		UserModel userModel = userService.findByUsername(principal.getName()).getFirst();
		model.addAttribute("user", userModel);
		return "admin/views/home";
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
	@GetMapping("/admin-management/add")
	public String addAccount(@RequestParam("type") String type){
		if (type.equals("course")){
			return "admin/views/createCourse";
		} else {
			return "admin/views/createAccount";
		}

	}
	@PostMapping("/admin-management/add")
	public String addAccount(@ModelAttribute RegisterDTO registerDTO){
		ModelMap modelMap = MapperUtil.getInstance().toModelMapFromDTO(registerDTO);
		return "redirect:/admin-management?type=" + registerDTO.getRole().toLowerCase();
	}
}
