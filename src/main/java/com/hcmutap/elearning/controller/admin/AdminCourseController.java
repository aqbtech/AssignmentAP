package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.dto.ClassResDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.service.IClassService;
import com.hcmutap.elearning.service.ICourseService;

import com.hcmutap.elearning.service.ISemesterService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String viewCourse(ModelMap model) {
		model.addAttribute("models", courseService.findAll());
		return "admin/views/view-table-course";
	}
	@GetMapping("/admin-management/update-course")
	public String updateCourse(@RequestParam("id") String id, ModelMap model) {
		try {
			CourseModel courseModel = courseService.findById(id);
			List<ClassModel> listClass = classService.getClassOfCourse(id);
			model.addAttribute("course", courseModel);
			model.addAttribute("classes", listClass);
			return "admin/views/view-table-class";
		} catch (Exception e) {
			return"redirect:/admin-management-course";
		}
	}

	@PostMapping("/admin-management/update-course")
	public String updateCourse(@RequestParam("id") String id, @ModelAttribute("course") CourseModel courseModel, ModelMap model){
		courseModel.setCourseId(id);
		try {
			courseModel.setFirebaseId(courseService.findById(id).getFirebaseId());
			model.addAttribute("message", "Khoá học " + courseModel.getCourseId()+ " đã được chỉnh sửa thành công!");
			courseService.update(courseModel);
			List<ClassModel> listClass = classService.getClassOfCourse(id);
			model.addAttribute("course", courseModel);
			model.addAttribute("classes", listClass);
			return "admin/views/view-table-class";
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/admin-management/deleteCourse")
	public String deleteCourse(@RequestParam("id") String id, final RedirectAttributes redirectAttributes) {

		try {
			CourseModel courseModel = courseService.findById(id);
			courseService.delete(courseModel.getFirebaseId());
			redirectAttributes.addFlashAttribute("message", "Xóa thành công khóa học " + courseModel.getCourseId());
			return "redirect:/admin-management-course";
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

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
		classModel.setSemesterId(classRes.getSemesterId());

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

	@GetMapping("/admin-management/update-class")
	public String updateClass (@RequestParam("id") String id, ModelMap model) {
		try {
			ClassModel classModel = classService.findById(id);
			ClassResDTO classRes = new ClassResDTO();
			classRes.setClassId(classModel.getClassId());
			classRes.setClassName(classModel.getClassName());
			classRes.setCourseId(classModel.getCourseId());
			classRes.setDayOfWeek(classModel.getDayOfWeek());
			classRes.setRoom(classModel.getRoom());
			classRes.setTimeStart(classModel.getTimeStart());
			classRes.setTimeStudy(transferTime2(classModel.getTimeStart(),classModel.getTimeEnd()));
			classRes.setSemesterId(classModel.getSemesterId());
			model.addAttribute("class", classRes);
			model.addAttribute("semester", semesterService.findAll());
			return "admin/views/updateClass";
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping("/admin-management/update-class")
	public String updateClass (@RequestParam("id") String id,
							   @ModelAttribute("class") ClassResDTO classRes, ModelMap model,
							   final RedirectAttributes redirectAttributes) {


		try {
			ClassModel classModel = classService.findById(id);
			classModel.setDayOfWeek(classRes.getDayOfWeek());
			classModel.setTimeStart(classRes.getTimeStart());
			classModel.setTimeEnd(transferTime(classRes.getTimeStart(), classRes.getTimeStudy()));
			classModel.setRoom(classRes.getRoom());
			classModel.setSemesterId(classRes.getSemesterId());
			List<ClassModel> listClass = classService.findAll();
			for(ClassModel cls : listClass) {
				if(!cls.getClassId().equals(classModel.getClassId()) && conflictTime(cls, classModel)) {
					model.addAttribute("message", "Trùng lịch với lớp " + cls.getClassName()
							+" của khóa học " + cls.getCourseId() + " (" + cls.getTimeStart() + "-" + cls.getTimeEnd()
							+ " " + cls.getDayOfWeek() + " " + cls.getRoom() + ")");
					classRes.setClassId(classModel.getClassId());
					model.addAttribute("class",classRes);
					model.addAttribute("semester",semesterService.findAll());
					return "admin/views/updateClass";
				}
			}
			classService.update(classModel);
			redirectAttributes.addFlashAttribute("message", "Chỉnh sửa thông tin lớp thành công!");
			return "redirect:/admin-management/update-course?id=" + classModel.getCourseId();
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/admin-management/deleteClass")
	public String deleteClass (@RequestParam("id") String id, final RedirectAttributes redirectAttributes) {

		try {
			ClassModel classModel = classService.findById(id);
			classService.delete(classModel.getFirebaseId());
			redirectAttributes.addFlashAttribute("message", "Xóa thành công lớp " + classModel.getClassName());
			return "redirect:/admin-management/update-course?id=" + classModel.getCourseId();
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
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
		String hour = (time.getHour() < 10) ? "0" + time.getHour() : "" + time.getHour();
		String minute = (time.getMinute() < 10) ? "0" + time.getMinute() : "" + time.getMinute();
		return hour + ":" + minute;
	}

	int transferTime2 (String timeStart, String timeEnd) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time1 = LocalTime.parse(timeStart, formatter);
		LocalTime time2 = LocalTime.parse(timeEnd, formatter);
		return time2.getHour() - time1.getHour();
	}
}
