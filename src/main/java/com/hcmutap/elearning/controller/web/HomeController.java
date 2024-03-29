package com.hcmutap.elearning.controller.web;

import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.service.IStudentService;
import com.hcmutap.elearning.service.impl.UserService;
import jakarta.annotation.Resource;
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
	@Resource
	private UserService userService;
	@RequestMapping(value = "/")
	public String index() {
		return "redirect:trang-chu";
	}
	@RequestMapping(value = "/trang-chu")
	public String home(ModelMap model){
		List<StudentModel> studentModelList = studentService.findAll();
//		model.addAttribute("students",studentModelList.get(0));
		StudentModel studentModel
				= new StudentModel("1", "Nguyen Van B", 20L, "nva@hcmut.edu.vn", "nva", "2210000", "B");
		model.addAttribute("students", studentModel);
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
}
