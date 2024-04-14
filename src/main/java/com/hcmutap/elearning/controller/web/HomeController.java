package com.hcmutap.elearning.controller.web;

import com.hcmutap.elearning.dto.InfoDTO;

import com.hcmutap.elearning.model.*;
import com.hcmutap.elearning.model.ClassModel;

import com.hcmutap.elearning.model.FileInfo;
import com.hcmutap.elearning.service.*;

import com.hcmutap.elearning.service.impl.CourseFacade;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

@Controller(value = "homeControllerOfWeb")
public class HomeController {
	@Resource
	private IStudentService studentService;
	@Resource
	private ITeacherService teacherService;
	@Resource
	private ICourseService courseService;
	@Resource
	private IInfoService infoService;
	@Resource
	private IUserService userService;
	@Resource
	private ISemesterService semesterService;
	private IFileService fileService;
	private ICourseFacade courseFacade;
	@Autowired
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}
	@Autowired
	public void setTeacherService(ITeacherService teacherService) {
		this.teacherService = teacherService;
	}
	@RequestMapping(value = "/")
	public String index() {
		return "redirect:trang-chu";
	}
	@RequestMapping(value = "/trang-chu")
	public String home(){
		return "web/views/home";
	}
	@RequestMapping(value = "/about")
	public String about(){
		return "web/views/about";
	}
	@GetMapping(value="/info")
	public String info(Principal principal, ModelMap model){
		try {
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			if (infoDTO.getRole().equalsIgnoreCase("ADMIN")) {
				model.addAttribute("message", "You are not a student or teacher");
				return "login/404_page";
			}
			model.addAttribute("user", infoDTO);
			return "web/views/view_info";
		} catch (Exception e) {
			model.addAttribute("message", "User not found");
			return "login/404_page";
		}
	}

	@RequestMapping("/service")
	public String service(ModelMap model, Principal principal) {
		try {
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			if (infoDTO.getRole().equalsIgnoreCase("ADMIN")) {
				model.addAttribute("message", "You are not a student or teacher");
				return "login/404_page";
			}
			String role = infoDTO.getRole();
			model.addAttribute("type", role);
			return "web/views/service";
		} catch (Exception e) {
			model.addAttribute("message", "User not found");
			return "login/404_page";
		}
	}

	@GetMapping(value="/course")
	public String course(@RequestParam("id") String id,
						 Principal principal, ModelMap model) {
		try {
			InfoDTO infoDTO = userService.getInfo(principal.getName());
			ClassModel classModel = null;
			InfoClassModel info = infoService.getClassInfo(id);
			if (infoDTO.getRole().equalsIgnoreCase("student")) {
				if (studentService.isExistStudentInClass(principal.getName(), id))
					classModel = CourseFacade.getInstance().getClassInfo(id);
				else {
					model.addAttribute("message", "You are not in this class");
					return "login/404_page";
				}
			} else if (infoDTO.getRole().equalsIgnoreCase("teacher")) {
				if (teacherService.isExistTeacherInClass(principal.getName(), id))
					classModel = CourseFacade.getInstance().getClassInfo(id);
				else {
					model.addAttribute("message", "You are not in this class");
					return "login/404_page";
				}
			} else {
				model.addAttribute("message", "You are not a student or teacher");
				return "login/404_page";
			}
			if (classModel == null) {
				model.addAttribute("message", "Class not found");
				return "login/404_page";
			} else {
				model.addAttribute("class", classModel);
				model.addAttribute("info", info);
				String courseName = courseService.findById(classModel.getCourseId()).getCourseName();
				model.addAttribute("courseName", courseName);
				return "web/views/view_course";
			}
		} catch (Exception e) {
			model.addAttribute("message", "Class not found");
			return "login/404_page";
		}
	}
	@GetMapping(value="/my-course")
	public String myCourse(@RequestParam(name = "semester", required = false) String hk,
						   Principal principal, ModelMap model){
		try {
			if (hk==null) {
				hk = "all";
			}
			model.addAttribute("hk", hk);

			InfoDTO infoDTO = userService.getInfo(principal.getName());
			List<ClassModel> classes = null;
			List<String> coursesName = new ArrayList<>();

			if (infoDTO.getRole().equalsIgnoreCase("student")) {
				classes = studentService.getAllClass(principal.getName());
			} else if (infoDTO.getRole().equalsIgnoreCase("teacher")) {
				classes = teacherService.getAllClass(principal.getName());
			} else {
				model.addAttribute("error", "You are not a student or teacher");
				return "login/404_page";
			}

			List<ClassModel> filteredClasses = new ArrayList<>();
			if (hk.equals("all")) {
				filteredClasses = classes;
			}
			else {
				for (ClassModel classModel : classes) {
					if (classModel.getSemesterId().equals(hk)) {
						filteredClasses.add(classModel);
					}
				}
			}
			model.addAttribute("classes", filteredClasses);


			List<SemesterModel> semesterList = semesterService.findAll();
			model.addAttribute("semesterList", semesterList);


			for (ClassModel classModel : classes) {
				CourseModel courseModel = courseService.findById(classModel.getCourseId());
				coursesName.add(courseModel.getCourseName());
			}

			model.addAttribute("coursesName", coursesName);

			List<String> listOfImageLinks = Arrays.asList(
					"https://i.imgur.com/ocueq8H.png",
					"https://i.imgur.com/derGMH0.png",
					"https://i.imgur.com/xz6aeKH.png",
					"https://i.imgur.com/TDfhls4.png",
					"https://i.imgur.com/EHXPUkU.png"
			);
			model.addAttribute("listOfImageLinks", listOfImageLinks);

			return "web/views/my_course";
		} catch (Exception e) {
			model.addAttribute("error", "User not found");
			return "login/404_page";
		}
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam(value = "file") MultipartFile file,
							 @RequestParam("folder") String folder,
							 @RequestParam("classId") String classId,
							 @RequestParam("infoId") String infoId,
							 @RequestParam("currentTitleForUpload") String currentTitle) {
		try {
			System.out.println(folder);
			FileInfo fileInfo = new FileInfo(folder, file.getOriginalFilename());
			fileService.uploadFile(file, fileInfo);
			InfoClassModel info = infoService.findById(infoId);
			List<Document> listDocument = info.getListDocument();
			Document matchedDocument = null;
			for (Document document : listDocument) {
				if (document.getTitle().equals(currentTitle)) {
					matchedDocument = document;
					break;
				}
			}
			infoService.addFile(infoId, matchedDocument, fileInfo);
			return "redirect:/course?id=" + classId;
		} catch (Exception e) {
			return "redirect:/course?id=" + classId;
		}
	}
	@GetMapping("/download/{folder}/{fileName}")
	public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String folder, @PathVariable String fileName) {
		FileInfo fileInfo = new FileInfo(folder, fileName);
		return fileService.downloadFile(fileInfo);
	}
	@PostMapping("/addGroup")
	public String addGroup(@RequestParam("groupTitle") String title,
						   @RequestParam("classId") String classId,
						   @RequestParam("infoId") String infoId) {
		Document document = new Document(title, new ArrayList<>());
		infoService.addDoc(infoId,document);
		return "redirect:/course?id=" + classId;
	}
	@PostMapping("/deleteGroup")
	public String deleteGroup(@RequestParam("groupToDelete") String title,
							  @RequestParam("classId") String classId,
							  @RequestParam("infoId") String infoId) {
		try {
			InfoClassModel info = infoService.findById(infoId);
			List<Document> listDocument = info.getListDocument();
			Document matchedDocument = null;
			for (Document document : listDocument) {
				if (document.getTitle().equals(title)) {
					matchedDocument = document;
					break;
				}
			}
            if(matchedDocument != null) {
				List<FileInfo> listFile = matchedDocument.getListFile();
				if (listFile != null) {
					for (FileInfo fileInfo : listFile) {
						infoService.deleteFile(infoId, matchedDocument, fileInfo);
						fileService.deleteFile(fileInfo);
					}
				}
				infoService.deleteDoc(infoId, matchedDocument);
			}
			return "redirect:/course?id=" + classId;
		} catch (Exception e) {
			return "redirect:/course?id=" + classId;
		}
	}
	@PostMapping("/saveNewTitle")
	public String saveNewTitle(@RequestParam("currentTitle") String currentTitle,
							   @RequestParam("newTitle") String newTitle,
							   @RequestParam("classId") String classId,
							   @RequestParam("infoId") String infoId) {
		try {
			InfoClassModel info = infoService.findById(infoId);
			List<Document> listDocument = info.getListDocument();
			Document matchedDocument = null;
			for (Document document : listDocument) {
				if (document.getTitle().equals(currentTitle)) {
					matchedDocument = document;
					break;
				}
			}
			infoService.updateTile(infoId, matchedDocument, newTitle);
			return "redirect:/course?id=" + classId;
		} catch (Exception e) {
			return "redirect:/course?id=" + classId;
		}
	}
	@GetMapping("/delete/{folder}/{fileName}/{classId}/{title}/{infoId}")
	public String delete(@PathVariable String folder, @PathVariable String fileName,
						 @PathVariable String classId, @PathVariable String title,@PathVariable String infoId){
		try {
			InfoClassModel info = infoService.findById(infoId);
			List<Document> listDocument = info.getListDocument();
			Document matchedDocument = null;
			for (Document document : listDocument) {
				if (document.getTitle().equals(title)) {
					matchedDocument = document;
					break;
				}
			}
			FileInfo fileInfo = new FileInfo(folder, fileName);
			infoService.deleteFile(infoId, matchedDocument, fileInfo);
			fileService.deleteFile(fileInfo);
			return "redirect:/course?id=" + classId;
		} catch (Exception e) {
			return "redirect:/course?id=" + classId;
		}
	}
}