package com.hcmutap.elearning.controller.web;

import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller(value = "homeControllerOfWeb")
public class HomeController {
	@Autowired
	private IStudentService studentService;
	@RequestMapping(value = "/")
	public String index() {
		return "redirect:trang-chu";
	}
	@RequestMapping(value = "/trang-chu")
	public String home(ModelMap model){
		List<StudentModel> studentModelList = studentService.findAll();
//		model.addAttribute("students",studentModelList.get(0));
		StudentModel studentModel
				= new StudentModel("1", "2210000", "Nguyen Van web");
		model.addAttribute("students", studentModel);
		return "web/views/home";
	}
	@RequestMapping(value = "/about")
	public String about(){
		return "web/views/about";
	}
}
