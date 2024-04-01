package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.dto.CourseDTO;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.service.ICourseService;
import com.hcmutap.elearning.utils.MapperUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminCourseController {
	@Resource
	private ICourseService courseService;
	// TODO: viết hàm addCourse để thêm một khóa học mới
	// tham khảo hàm addAccount trong HomeController.java(GetMapping và PostMapping)
	// yêu cầu nhận vào 1 html form (tên form là createCourse đã có trong resourses/templates/admin/views/createCourse.html)
	// va sau do goi service de them vao database
	@GetMapping("/admin-management/add-course")
	public String addCourse() {
		return "admin/views/createCourse";
	}
	@PostMapping("/admin-management/add-course")
	public String addCourse(HttpServletRequest request) {
		CourseModel courseModel = new CourseModel();
		courseModel.setCourseId(request.getParameter("courseId"));
		courseModel.setCourseName(request.getParameter("courseName"));
		courseModel.setCredit(Integer.parseInt(request.getParameter("credit")));
		courseModel.setPercentBT(Double.parseDouble(request.getParameter("percentBT")));
		courseModel.setPercentBTL(Double.parseDouble(request.getParameter("percentBTL")));
		courseModel.setPercentGK(Double.parseDouble(request.getParameter("percentGK")));
		courseModel.setPercentCK(Double.parseDouble(request.getParameter("percentCK")));
		courseService.save(courseModel);
		return "redirect:/admin-management?type=" + "course";
	}


	// TODO: addClasses để thêm một lớp học mới

}
