package com.hcmutap.elearning.controller.web;

import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IStudentService;
import com.hcmutap.elearning.service.ITeacherService;
import com.hcmutap.elearning.service.impl.CourseFacade;
import com.hcmutap.elearning.service.impl.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller(value = "homeControllerOfWeb")
public class HomeController {
	@Resource
	private IStudentService studentService;
	private ITeacherService teacherService;
	@Autowired
	public void setTeacherService(ITeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@Resource
	private UserService userService;
	@RequestMapping(value = "/")
	public String index() {
		return "redirect:trang-chu";
	}
	@RequestMapping(value = "/trang-chu")
	public String home(ModelMap model){
		return "web/views/home";
	}
	@RequestMapping(value = "/about")
	public String about(){
		return "web/views/about";
	}
	@GetMapping(value="/info")
	public String info(Principal principal, ModelMap model){
		InfoDTO infoDTO = userService.getInfo(principal.getName());
		model.addAttribute("user", infoDTO);
		return "web/views/view_info";
	}
	@GetMapping(value="/course")
	public String course(Principal principal, ModelMap model){
		InfoDTO infoDTO = userService.getInfo(principal.getName());
		List<ClassModel> classes = null;
		if (infoDTO.getRole().equalsIgnoreCase("student")){
			classes = studentService.getAllClass(principal.getName());
		} else if (infoDTO.getRole().equalsIgnoreCase("teacher")){
			classes = teacherService.getAllClass(principal.getName());
		} else {
			model.addAttribute("error", "You are not a student or teacher");
			return "login/404_page";
		}
		model.addAttribute("classes", classes);
		return "web/views/view_course";
	}
}
