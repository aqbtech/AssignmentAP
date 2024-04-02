package com.hcmutap.elearning.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {
	@GetMapping(value = "/course")
	public String teacher(@RequestParam("id") int id) {
		return "web/views/view_course_teacher";
	}
}
