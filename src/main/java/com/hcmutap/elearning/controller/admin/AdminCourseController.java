package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.dto.ClassResDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.SemesterModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.service.*;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminCourseController {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Resource
	private ICourseService courseService;
	@Resource
	private IClassService classService;
	@Resource
	private IPointService pointService;
	@Resource
	private ISemesterService semesterService;
	@Resource
	private ITeacherService teacherService;

	@GetMapping("/admin-management-course")
	public String viewCourse(ModelMap model, @RequestParam(required = false) String keyword,
							 @RequestParam(defaultValue = "1") Integer page,
							 @RequestParam(defaultValue = "3") Integer size) {
		Page<CourseModel> coursePage = courseService.getPage(keyword, page, size);
		List<CourseModel> courseModels = coursePage.getContent();
		model.addAttribute("models", courseModels);
		model.addAttribute("currentPage", coursePage.getNumber() + 1);
		model.addAttribute("totalItems", coursePage.getTotalElements());
		model.addAttribute("totalPages", coursePage.getTotalPages());
		model.addAttribute("pageSize", coursePage.getSize());
//			model.addAttribute("models", studentService.findAll());
		model.addAttribute("type", "student");
		model.addAttribute("models", courseService.findAll());
		return "admin/views/view-table-course";
	}
	@GetMapping("/admin-management/update-course")
	public String updateCourse(@RequestParam("id") String id, ModelMap model,
							   final RedirectAttributes redirectAttributes, @RequestParam(required = false) String keyword,
							   @RequestParam(defaultValue = "1") int page,
							   @RequestParam(defaultValue = "3") int size) {
		try {
			CourseModel courseModel = courseService.findById(id);
			model.addAttribute("course", courseModel);
			Page<ClassModel> pageResult = classService.getPage(keyword, courseModel.getCourseId(), page, size);
			model.addAttribute("updateLink", "admin-management/update-class");
			model.addAttribute("deleteLink", "admin-management/deleteClass");
			model.addAttribute("href", "/admin-management/update-course?id=" + id);
			model.addAttribute("type", "class");
			genePage(model, pageResult);
			return "admin/views/view-table-class";
		} catch (Exception e) {
			logger.error("Error in updateCourse {}", id);
			// implFunc to redirect to error page and message
			redirectAttributes.addFlashAttribute("message", "Hiện không thể tìm thấy khóa học " + id);
			return "redirect:/admin-management?type=course";
		}
	}

	void genePage(ModelMap model, Page<?> page) {
		model.addAttribute("models", page.getContent());
		model.addAttribute("currentPage", page.getNumber() + 1);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("pageSize", page.getSize());
	}

	@PostMapping("/admin-management/update-course")
	public String updateCourse(@RequestParam("id") String id, @ModelAttribute("course") CourseModel courseModel, ModelMap model, final RedirectAttributes redirectAttributes){
		courseModel.setCourseId(id);
		try {
			CourseModel course = courseService.findById(id);
			courseModel.setFirebaseId(course.getFirebaseId());
			redirectAttributes.addFlashAttribute("message", "Khoá học " + courseModel.getCourseId()+ " đã được chỉnh sửa thành công!");
			courseService.update(courseModel);
			return "redirect:/admin-management/update-course?id=" + id;
		} catch (NotFoundException e) {
			logger.error("Error in updateCourse Postmethod");
			// implFunc to redirect to error page and message
			redirectAttributes.addFlashAttribute("message", "Hiện không thể tìm thấy khóa học " + id);
			return "redirect:/admin-management?type=course";
		}
	}

	@GetMapping("/admin-management/deleteCourse")
	public String deleteCourse(@RequestParam("id") String id,
							   final RedirectAttributes redirectAttributes) {
		try {
			CourseModel courseModel = courseService.findById(id);
			List<ClassModel> cls = classService.getClassOfCourse(id);
			if(cls.isEmpty()) {
				courseService.delete(courseModel.getFirebaseId());
				redirectAttributes.addFlashAttribute("message", "Xóa thành công khóa học " + courseModel.getCourseId());
			} else {
				redirectAttributes.addFlashAttribute("message", "Khóa học vẫn còn lớp, không thể xóa");
			}
			return "redirect:/admin-management?type=course";
		} catch (NotFoundException e) {
			logger.error("Error in deleteCourse");
			// implFunc to redirect to error page and message
			redirectAttributes.addFlashAttribute("message", "Xóa không thành công, không thể tìm thấy khóa học " + id);
			return "redirect:/admin-management?type=course";
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
		try {
			courseService.findById(courseModel.getFirebaseId());
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
		try{
			model.addAttribute("courses", courseService.findAll());
			model.addAttribute("semester", semesterService.findAll());
			if(!model.containsAttribute("class"))
				model.addAttribute("class",new ClassResDTO());
			return "admin/views/createClass";
		}catch (Exception e){
			logger.error(String.valueOf(new RuntimeException(e)));
			return "redirect:/login/Rare_fault";
		}
	}

	@PostMapping("/admin-management/add-class")
	public String addClass(@ModelAttribute("class") ClassResDTO classRes, ModelMap model) {
		try {
			ClassModel classModel = new ClassModel();
			classModel.setCourseId(classRes.getCourseId());
			classModel.setClassName(classRes.getClassName());
			classModel.setClassId(classModel.getCourseId() + "-" + classModel.getClassName() + "-" + classRes.getSemesterId());
			classModel.setDayOfWeek(classRes.getDayOfWeek());
			classModel.setTimeStart(classRes.getTimeStart());
			classModel.setTimeEnd(convertTime(classRes.getTimeStart(), classRes.getTimeStudy()));
			classModel.setRoom(classRes.getRoom());
			classModel.setSemesterId(classRes.getSemesterId());

			boolean notSave = false;
			List<ClassModel> listClass = classService.findAll();
			for (ClassModel cls : listClass) {
				if (cls.getClassId().equals(classModel.getClassId())) {
					model.addAttribute("message", "Lớp " + classModel.getClassName()
							+ " của khóa học " + classModel.getCourseId() + " đã tồn tại!");
					notSave = true;
					model.addAttribute("class", classRes);
					model.addAttribute("error", "error");
					break;
				} else if (cls.getSemesterId().equals(classModel.getSemesterId()) && conflictTime(cls, classModel)) {
					model.addAttribute("message", "Trùng lịch với lớp " + cls.getClassName()
							+ " của khóa học " + cls.getCourseId() + " (" + cls.getTimeStart() + "-" + cls.getTimeEnd()
							+ " " + cls.getDayOfWeek() + " " + cls.getRoom() + ")");
					notSave = true;
					model.addAttribute("class", classRes);
					model.addAttribute("error", "error");
					break;
				}
			}
			if (!notSave) {
				model.addAttribute("message", "Lớp học đã được tạo thành công!");
				classService.save(classModel);
				model.addAttribute("class", new ClassResDTO());
			}
			model.addAttribute("courses", courseService.findAll());
			model.addAttribute("semester", semesterService.findAll());
			return "admin/views/createClass";
		}
		catch (Exception e){
			logger.error(String.valueOf(new RuntimeException(e)));
			return "redirect:/login/Rare_fault";
		}
	}

	@GetMapping("/admin-management/update-class")
	public String updateClass (@RequestParam("id") String id, ModelMap model, final RedirectAttributes redirectAttributes) {
		try {
			ClassModel classModel = classService.findById(id);
			ClassResDTO classRes = new ClassResDTO();
			classRes.setClassId(classModel.getClassId());
			classRes.setClassName(classModel.getClassName());
			classRes.setCourseId(classModel.getCourseId());
			classRes.setDayOfWeek(classModel.getDayOfWeek());
			classRes.setRoom(classModel.getRoom());
			classRes.setTimeStart(classModel.getTimeStart());
			classRes.setTimeStudy(convertTime2(classModel.getTimeStart(),classModel.getTimeEnd()));
			classRes.setSemesterId(classModel.getSemesterId());
			model.addAttribute("class", classRes);
			model.addAttribute("semester", semesterService.findAll());
			return "admin/views/updateClass";
		} catch (NotFoundException e) {
			logger.error("Error in updateClass");
			// implFunc to redirect to error page and message
			redirectAttributes.addFlashAttribute("message", "Không tìm thế lớp học " + id);
			String[] parts = id.split("-");
			return "redirect:/admin-management/update-course?id=" + parts[0];
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
			classModel.setTimeEnd(convertTime(classRes.getTimeStart(), classRes.getTimeStudy()));
			classModel.setRoom(classRes.getRoom());
			List<ClassModel> listClass = classService.findAll();
			for(ClassModel cls : listClass) {
				if(!cls.getClassId().equals(classModel.getClassId())
						&& cls.getSemesterId().equals(classModel.getSemesterId())
						&& conflictTime(cls, classModel)) {
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
			logger.error("Error in updateClass Postmethod");
			// implFunc to redirect to error page and message
			redirectAttributes.addFlashAttribute("message", "Chỉnh sửa không thành công, không tìm thế lớp học " + id);
			String[] parts = id.split("-");
			return "redirect:/admin-management/update-course?id=" + parts[0];
		}
	}
	@GetMapping("/admin-management/delete-class")
	public String forceDelClass (@RequestParam("id") String id,
								 final RedirectAttributes redirectAttributes) {
		try {
			ClassModel classModel = classService.findById(id);
			TeacherModel teacher;
			try {
				teacher = teacherService.findById(classModel.getTeacherId());
				int i = 0;
				List<String> classes = teacher.getClasses();
				while(i < classes.size()){
					if(classes.get(i).equals(classModel.getClassId()))
						break;
					++i;
				}
				if(i != classes.size()) {
					teacher.getClasses().remove(i);
					teacher.getCourses().remove(i);
					teacherService.update(teacher);
				}
				classService.delete(classModel.getFirebaseId());
				redirectAttributes.addFlashAttribute("message", "Xóa thành công lớp " + classModel.getClassName());
			} catch (Exception exception) {
				classService.delete(classModel.getFirebaseId());
				redirectAttributes.addFlashAttribute("message", "Xóa thành công lớp " + classModel.getClassName());
			}
			return "redirect:/admin-management/update-course?id=" + classModel.getCourseId();
		} catch (NotFoundException e) {
			logger.error("Error in deleteClass");
			// implFunc to redirect to error page and message
			redirectAttributes.addFlashAttribute("message", "Xóa không thành công, không tìm thế lớp học " + id);
			String[] parts = id.split("-");
			return "redirect:/admin-management/update-course?id=" + parts[0];
		}
	}

	@GetMapping("/admin-management/deleteClass")
	public String deleteClass (@RequestParam("id") String id,
							   final RedirectAttributes redirectAttributes) {
		try {
			ClassModel classModel = classService.findById(id);
			try {
				pointService.getListStudentByClassId(classModel.getClassId());
				redirectAttributes.addFlashAttribute("message", "Vẫn còn sinh viên trong lớp, không thể xóa!");
			} catch (NotFoundException e) {
				TeacherModel teacher;
				try {
					teacher = teacherService.findById(classModel.getTeacherId());
					redirectAttributes.addFlashAttribute("message", "Lớp vẫn có giáo viên " + teacher.getFullName() + ", chắc chắn xóa");
					redirectAttributes.addFlashAttribute("force", true);
					redirectAttributes.addFlashAttribute("id",id);
					return "redirect:/admin-management/update-course?id=" + classModel.getCourseId();
				} catch (Exception exception) {
					classService.delete(classModel.getFirebaseId());
					redirectAttributes.addFlashAttribute("message", "Xóa thành công lớp " + classModel.getClassName());
				}
			}
			return "redirect:/admin-management/update-course?id=" + classModel.getCourseId();
		} catch (NotFoundException e) {
			logger.error("Error in deleteClass");
			// implFunc to redirect to error page and message
			redirectAttributes.addFlashAttribute("message", "Xóa không thành công, không tìm thế lớp học " + id);
			String[] parts = id.split("-");
			return "redirect:/admin-management/update-course?id=" + parts[0];
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
	String convertTime(String timeStart, int timeStudy) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time = LocalTime.parse(timeStart, formatter);
		time = time.plusHours(timeStudy);
		String hour = (time.getHour() < 10) ? "0" + time.getHour() : "" + time.getHour();
		String minute = (time.getMinute() < 10) ? "0" + time.getMinute() : "" + time.getMinute();
		return hour + ":" + minute;
	}

	int convertTime2(String timeStart, String timeEnd) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time1 = LocalTime.parse(timeStart, formatter);
		LocalTime time2 = LocalTime.parse(timeEnd, formatter);
		return time2.getHour() - time1.getHour();
	}

	@GetMapping("/admin-management-semester")
	public String viewSemester (ModelMap model) {
		try{
			List<SemesterModel> semesterList = semesterService.findAll();
			semesterList = sortSemester(semesterList);
			model.addAttribute("semesters", semesterList);
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String date = currentDate.format(formatter);
			model.addAttribute("currentDate", date);
			return "admin/views/viewSemester";
		}catch (Exception e){
			logger.error(String.valueOf(new RuntimeException(e)));
			return "redirect:/login/Rare_fault";
		}
	}

	@GetMapping("/admin-management/add-semester")
	public String addSemester(ModelMap map) {
		map.addAttribute("semester", new SemesterModel());
		return "admin/views/createSemester";
	}

	@PostMapping("/admin-management/add-semester")
	public String addSemester(@ModelAttribute("semester") SemesterModel semesterModel, ModelMap model,
							  final RedirectAttributes redirectAttributes) {
		try{
			if(semesterService.findById(semesterModel.getSemesterName()) != null) {
				model.addAttribute("message", "Không thành công! Học kì đã được tạo trước đó!");
				semesterModel.setId(semesterModel.getSemesterName());
				model.addAttribute("semester", semesterModel);
				return "admin/views/createSemester";
			} else {
				semesterModel.setId(semesterModel.getSemesterName());
				semesterService.save(semesterModel);
				redirectAttributes.addFlashAttribute("message", "Học kì đã tạo thành công!");
				return "redirect:/admin-management?type=semester";
			}
		} catch (Exception e){
			semesterModel.setId(semesterModel.getSemesterName());
			semesterService.save(semesterModel);
			redirectAttributes.addFlashAttribute("message", "Học kì đã tạo thành công!");
			return "redirect:/admin-management?type=semester";
		}
	}

	@GetMapping("/admin-management/update-semester")
	public String updateSemester(@RequestParam("id") String id,
								 ModelMap model, final RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("semester", semesterService.findById(id));
			return "admin/views/updateSemester";
		} catch (NotFoundException e) {
			logger.error("Error in updateSemester");
			// implFunc to redirect to error page and message
			redirectAttributes.addFlashAttribute("message", "Không tìm thấy học kì " + id);
			return "redirect:/admin-management?type=semester";
		}
	}
	@PostMapping("/admin-management/update-semester")
	public String updateSemester(@RequestParam("id") String id,
								 @ModelAttribute SemesterModel semesterModel,
								 final RedirectAttributes redirectAttributes) {
		try {
			SemesterModel semester = semesterService.findById(id);
			semester.setStartDate(semesterModel.getStartDate());
			semester.setEndDate(semesterModel.getEndDate());
			semesterService.update(semester);
			redirectAttributes.addFlashAttribute("message", "Chỉnh sửa thành công");
			return "redirect:/admin-management?type=semester";
		} catch (NotFoundException e) {
			logger.error("Error in updateSemester Postmethod");
			// implFunc to redirect to error page and message
			redirectAttributes.addFlashAttribute("message", "Không tìm thấy học kì " + id);
			return "redirect:/admin-management?type=semester";
		}
	}
	@GetMapping("/admin-management/change-semester")
	public String changeSemester(@RequestParam("id") String id,
								 final RedirectAttributes redirectAttributes) {
		try {
			SemesterModel semester = semesterService.findById(id);
			List<SemesterModel> semesterList = semesterService.findAll();
			semesterList = sortSemester(semesterList);
			if(semester.getId().equals(semesterList.getLast().getId())) {
				redirectAttributes.addFlashAttribute("message", "Cần tạo học kì tiếp theo trước khi kết thúc học kì hiện tại");
				return "redirect:/admin-management-semester";
			} else {
				for(int i = 0; i < semesterList.size(); ++i) {
					if(semester.getSemesterName().equals(semesterList.get(i).getSemesterName())) {
						LocalDate currentDate = LocalDate.now();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						String date = currentDate.format(formatter);
						semester.setEndDate(date);
						semesterList.get(i + 1).setStartDate(date);
						semesterService.update(semester);
						semesterService.update(semesterList.get(i + 1));
						redirectAttributes.addFlashAttribute("message", "Kết thúc học kì thành công");
						return "redirect:/admin-management-semester";
					}
				}
			}
			redirectAttributes.addFlashAttribute("message", "Có lỗi vui lòng thử lại");
			return "redirect:/admin-management-semester";

		} catch(NotFoundException e) {
			logger.error("Error in changeSemester");
			// implFunc to redirect to error page and message
			redirectAttributes.addFlashAttribute("message", "Không tìm thấy học kì " + id);
			return "redirect:/admin-management?type=semester";
		}
	}

	@GetMapping("/admin-management/delete-semester")
	public String deleteSemester (@RequestParam("id") String id,
								  final RedirectAttributes redirectAttributes) {
		try {
			SemesterModel semesterModel = semesterService.findById(id);
			try{
				List<ClassModel> cls = classService.findBy("semesterId", id);
				if(cls != null) {
					List<String> lst = new ArrayList<>();
					lst.add(semesterModel.getFirebaseId());
					semesterService.delete(lst);
					redirectAttributes.addFlashAttribute("message", "Xóa học kì thành công");
					return "redirect:/admin-management?type=semester";
				} else {
					redirectAttributes.addFlashAttribute("message", "Vẫn còn lớp ở học kì này, không thể xóa");
					return "redirect:/admin-management?type=semester";
				}
			} catch (NotFoundException e) {
				List<String> lst = new ArrayList<>();
				lst.add(semesterModel.getFirebaseId());
				semesterService.delete(lst);
				redirectAttributes.addFlashAttribute("message", "Xóa học kì thành công");
				return "redirect:/admin-management?type=semester";
			}
		} catch(NotFoundException e) {
			logger.error("Error in deleteSemester");
			// implFunc to redirect to error page and message
			redirectAttributes.addFlashAttribute("message", "Không tìm thấy học kì " + id);
			return "redirect:/admin-management?type=semester";
		}
	}

	List<SemesterModel> sortSemester (List<SemesterModel> semesters) {
		List<SemesterModel> list = new ArrayList<>(semesters);
		for(int j = 0; j < list.size() - 1; ++j){
			int minIndex = j;
			for(int i = j + 1; i < list.size(); ++i) {
				if (Integer.parseInt(list.get(i).getSemesterName()) < Integer.parseInt(list.get(minIndex).getSemesterName())) {
					minIndex = i;
				}
			}
			if(minIndex != j) {
				SemesterModel semester = list.get(minIndex);
				list.set(minIndex, list.get(j));
				list.set(j, semester);
			}
		}
		return list;
	}
}
