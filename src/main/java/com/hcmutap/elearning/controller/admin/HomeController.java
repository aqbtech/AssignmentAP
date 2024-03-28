package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller(value = "homeControllerOfAdmin")
public class HomeController {
	@Autowired
	private IStudentService studentService;

	@RequestMapping(value = "/admin-home")
	public String index(ModelMap model){
		List<StudentModel> studentModelList = studentService.findAll();
//		model.addAttribute("students",studentModelList.get(0));
		StudentModel studentModel
				= new StudentModel("1", "2210000", "Nguyen Van B", 1, " ", " ");
		model.addAttribute("students", studentModel);
		return "admin/views/home";
	}
	@RequestMapping(value = "/admin-add-student")
	public String addStudent(ModelMap model){
		StudentModel studentModel = new StudentModel();
		model.addAttribute("student", studentModel);
		return "admin/views/add-student";
	}
}
