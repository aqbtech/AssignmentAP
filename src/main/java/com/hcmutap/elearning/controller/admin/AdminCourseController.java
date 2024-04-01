package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.controller.api.ClassAPI;
import com.hcmutap.elearning.controller.api.CourseAPI;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminCourseController {
	@Resource
	private CourseAPI courseAPI;
	@Resource
	private ClassAPI classAPI;
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
		courseAPI.save(courseModel);
		return "redirect:/admin-management/add-course";
	}


	// TODO: addClasses để thêm một lớp học mới
	@GetMapping("/admin-management/add-class")
	public String addClass(ModelMap model) {
		model.addAttribute("courses", courseAPI.findAll());
		return "admin/views/createClass";
	}

	@PostMapping("/admin-management/add-class")
	public String addClass(HttpServletRequest request) {
		ClassModel classModel = new ClassModel();
		classModel.setCourseId((request.getParameter("courseId")));
		classModel.setClassName(request.getParameter("className"));
		classModel.setClassId(classModel.getCourseId()+"-"+classModel.getClassName());
		classModel.setDayOfWeek(request.getParameter("dayOfWeek"));
		classModel.setTimeStart(request.getParameter("timeStart"));
		classModel.setTimeEnd(request.getParameter("timeEnd"));
		classModel.setRoom(request.getParameter("room"));
		classAPI.save(classModel);
		return "redirect:/admin-management/add-class";
	}
}
