package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.dto.ClassResDTO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.service.IClassService;
import com.hcmutap.elearning.service.ICourseService;

import com.hcmutap.elearning.service.ISemesterService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class AdminCourseController {
	@Resource
	private ICourseService courseService;
	@Resource
	private IClassService classService;

	@Resource
	private ISemesterService semesterService;

	@GetMapping("/admin-management-course")
	public String viewCourse(ModelMap model,
							 @RequestParam("action") String action,
							 @RequestParam("id") String id) {
		if(action.equals("delete")) {
			CourseModel courseModel;
			try{
				courseModel = courseService.findById(id);
			} catch(Exception e) {
				return"redirect:/admin-management-course?action=view&id=";
			}
			List<ClassModel> listClass = courseService.getLichTrinh(id);
			if(!listClass.isEmpty()) {
				String message = "Cần xóa các lớp ";
				for (ClassModel cls : listClass) {
					message += cls.getClassName() + " ";
				}
				message += " của khóa học " + id + " trước khi xóa khóa học!";
				model.addAttribute("message", message);
			} else {
				courseService.delete(courseModel.getFirebaseId());
				model.addAttribute("message","Xóa thành công khóa học " + id +"!");
			}
		}
		model.addAttribute("models", courseService.findAll());
		return "admin/views/view-table-course";
	}
	@GetMapping("/admin-management/update-course")
	public String updateCourse(@RequestParam("id") String id, ModelMap model) {
		CourseModel courseModel;
		try{
			courseModel = courseService.findById(id);
		} catch (Exception e) {
			return"redirect:/admin-management-course?action=view&id=";
		}
		List<ClassModel> listClass = classService.getClassOfCourse(id);
		model.addAttribute("course", courseModel);
		model.addAttribute("classes", listClass);
		return "admin/views/view-table-class";
	}

	@PostMapping("/admin-management/update-course")
	public String updateCourse(@RequestParam("id") String id, @ModelAttribute("course") CourseModel courseModel, ModelMap model){
		courseModel.setCourseId(id);
		courseModel.setFirebaseId(courseService.findById(id).getFirebaseId());
		model.addAttribute("message", "Khoá học " + courseModel.getCourseId()+ " đã được chỉnh sửa thành công!");
		courseService.update(courseModel);
		List<ClassModel> listClass = classService.getClassOfCourse(id);
		model.addAttribute("course", courseModel);
		model.addAttribute("classes", listClass);
		return "admin/views/view-table-class";
	}


	// TODO: viết hàm addCourse để thêm một khóa học mới
	// tham khảo hàm addAccount trong HomeController.java(GetMapping và PostMapping)
	// yêu cầu nhận vào 1 html form (tên form là createCourse đã có trong resourses/templates/admin/views/createCourse.html)
	// va sau do goi service de them vao database
	@GetMapping("/admin-management/add-course")
	public String addCourse(ModelMap model) {
		if(!model.containsAttribute("course"))
			model.addAttribute("course", new CourseModel());
		return "admin/views/createCourse";
	}
	@PostMapping("/admin-management/add-course")
	public String addCourse(@ModelAttribute("course") CourseModel courseModel, ModelMap model) {
		try{
			courseService.findById(courseModel.getCourseId());
			model.addAttribute("message", "Khóa học " + courseModel.getCourseId() +" đã tồn tại!");
			model.addAttribute("course",courseModel);
			model.addAttribute("error","error");
		} catch (Exception e) {
			model.addAttribute("message", "Khoá học " + courseModel.getCourseId()+ " đã được tạo thành công!");
			courseService.save(courseModel);
			model.addAttribute("course", new CourseModel());
		}
		return "admin/views/createCourse";
	}


	// TODO: addClasses để thêm một lớp học mới
	@GetMapping("/admin-management/add-class")
	public String addClass(ModelMap model) {

		model.addAttribute("courses", courseService.findAll());
		model.addAttribute("semester", semesterService.findAll());
		if(!model.containsAttribute("class"))
			model.addAttribute("class",new ClassResDTO());
		return "admin/views/createClass";
	}

	@PostMapping("/admin-management/add-class")
	public String addClass(@ModelAttribute("class") ClassResDTO classRes, ModelMap model) {
		ClassModel classModel = new ClassModel();
		classModel.setCourseId(classRes.getCourseId());
		classModel.setClassName(classRes.getClassName());
		classModel.setClassId(classModel.getCourseId()+"-"+classModel.getClassName());
		classModel.setDayOfWeek(classRes.getDayOfWeek());
		classModel.setTimeStart(classRes.getTimeStart());
		classModel.setTimeEnd(transferTime(classRes.getTimeStart(), classRes.getTimeStudy()));
		classModel.setRoom(classRes.getRoom());
		classModel.setSemester(classRes.getSemester());

		boolean notSave = false;
		List<ClassModel> listClass = classService.findAll();
		for(ClassModel cls : listClass) {
			if(cls.getClassId().equals(classModel.getClassId())) {
				model.addAttribute("message", "Lớp " + classModel.getClassName()
						+" của khóa học " + classModel.getCourseId() + " đã tồn tại!");
				notSave = true;
				model.addAttribute("class",classRes);
				model.addAttribute("error","error");
				break;
			} else if(conflictTime(cls, classModel)) {
				model.addAttribute("message", "Trùng lịch với lớp " + cls.getClassName()
						+" của khóa học " + cls.getCourseId() + " (" + cls.getTimeStart() + "-" + cls.getTimeEnd()
						+ " " + cls.getDayOfWeek() + " " + cls.getRoom() + ")");
				notSave = true;
				model.addAttribute("class",classRes);
				model.addAttribute("error","error");
				break;
			}
		}
		if(!notSave) {
			model.addAttribute("message", "Lớp học đã được tạo thành công!");
			classService.save(classModel);
			model.addAttribute("class",new ClassResDTO());
		}
		model.addAttribute("courses", courseService.findAll());
		model.addAttribute("semester", semesterService.findAll());
		return "admin/views/createClass";
	}

	boolean conflictTime (ClassModel a, ClassModel b) {
		if(a.getRoom().equals(b.getRoom()) && a.getDayOfWeek().equals(b.getDayOfWeek())) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			LocalTime timeStartA = LocalTime.parse(a.getTimeStart(), formatter);
			LocalTime timeEndA = LocalTime.parse(a.getTimeEnd(), formatter);
			LocalTime timeStartB = LocalTime.parse(b.getTimeStart(), formatter);
			LocalTime timeEndB = LocalTime.parse(b.getTimeEnd(), formatter);
			return (timeStartA.isBefore(timeEndB) && timeStartB.isBefore(timeEndA));
		}
		return false;
	}
	String transferTime (String timeStart, int timeStudy) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time = LocalTime.parse(timeStart, formatter);
		time = time.plusHours(timeStudy);
		return time.getHour() + ":" + time.getMinute();
	}
}
