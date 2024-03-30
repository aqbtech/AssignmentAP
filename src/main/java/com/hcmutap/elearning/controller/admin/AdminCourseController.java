package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.dto.CourseDTO;
import com.hcmutap.elearning.utils.MapperUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminCourseController {
	// TODO: viết hàm addCourse để thêm một khóa học mới
	// tham khảo hàm addAccount trong HomeController.java(GetMapping và PostMapping)
	// yêu cầu nhận vào 1 html form (tên form là createCourse đã có trong resourses/templates/admin/views/createCourse.html)
	// va sau do goi service de them vao database
	@GetMapping("/admin-management/addCourse")
	public String addCourse(@RequestParam("type ") String type) {


		return "admin/views/createCourse";
	}
	@PostMapping("/admin-management/addCourse")
	public String addCourse(@ModelAttribute CourseDTO courseDTO) {
		ModelMap modelMap = MapperUtil.getInstance().toModelMapFromDTO(courseDTO);
		return "redirect:/admin-management?type=" + courseDTO.getRole().toLowerCase();
	}


	// TODO: addClasses để thêm một lớp học mới

}
