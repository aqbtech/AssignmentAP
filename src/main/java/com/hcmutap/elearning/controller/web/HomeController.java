package com.hcmutap.elearning.controller.web;

import com.hcmutap.elearning.dto.InfoDTO;

import com.hcmutap.elearning.model.*;
import com.hcmutap.elearning.model.ClassModel;

import com.hcmutap.elearning.model.FileInfo;
import com.hcmutap.elearning.service.IFileService;
import com.hcmutap.elearning.service.IStudentService;
import com.hcmutap.elearning.service.ITeacherService;
import com.hcmutap.elearning.service.IUserService;

import jakarta.annotation.Resource;
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

@Controller(value = "homeControllerOfWeb")
public class HomeController {
	@Resource
	private IStudentService studentService;
	@Resource
	private ITeacherService teacherService;
	@Resource
	private IUserService userService;
	private IFileService fileService;
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
		InfoDTO infoDTO = userService.getInfo(principal.getName());
		model.addAttribute("user", infoDTO);
		return "web/views/view_info";
	}
	@GetMapping(value="/course")
	public String course(Principal principal, ModelMap model){
		InfoDTO infoDTO = userService.getInfo(principal.getName());
		List<ClassModel> classes = null;
		if (infoDTO.getRole().equalsIgnoreCase("student")){
			classes = studentService.getAllClass(principal.getName());
		} else if (infoDTO.getRole().equalsIgnoreCase("teacher")){
			classes = teacherService.getAllClass(principal.getName());
		} else {
			model.addAttribute("message", "You are not a student or teacher");
			return "login/404_page";
		}
		model.addAttribute("classes", classes);
		return "web/views/view_course";
	}
	@GetMapping(value="/my-course")
	public String myCourse(Principal principal, ModelMap model){
		InfoDTO infoDTO = userService.getInfo(principal.getName());
		List<CourseModel> courses = null;

		if (infoDTO.getRole().equalsIgnoreCase("student")){
			StudentModel studentModel = studentService.findByUsername(principal.getName());
			// TODO: cap nhat lai theo ham moi
//			courses = studentModel.getCourses();
		} else if (infoDTO.getRole().equalsIgnoreCase("teacher")) {
			TeacherModel teacherModel = teacherService.findByUsername(principal.getName());
			//courses = teacherModel.getCourses();
		} else {
			model.addAttribute("error", "You are not a student or teacher");
			return "login/404_page";
		}
		model.addAttribute("courses", courses);

		List<String> listOfImageLinks = Arrays.asList(
				"https://i.imgur.com/ocueq8H.png",
				"https://i.imgur.com/derGMH0.png",
				"https://i.imgur.com/xz6aeKH.png",
				"https://i.imgur.com/TDfhls4.png",
				"https://i.imgur.com/EHXPUkU.png"
		);
		model.addAttribute("listOfImageLinks", listOfImageLinks);

		return "web/views/my_course";
  	}
	@PostMapping("/upload")
	public String uploadFile(@RequestParam(value = "file") MultipartFile file, @RequestParam("folder") String folder) {
		FileInfo fileInfo = new FileInfo(folder, file.getOriginalFilename());
		fileService.uploadFile(file, fileInfo);
		return "redirect:/trang-chu"; // redirect to course page
	}
	@GetMapping("/download/{folder}/{fileName}")
	public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String folder, @PathVariable String fileName) {
		FileInfo fileInfo = new FileInfo(folder, fileName);
		return fileService.downloadFile(fileInfo);
	}
	@DeleteMapping({"/delete/{folder}/{fileName}", "/delete/{fileName}"})
	public String deleteFile(@PathVariable(required = false) String folder, @PathVariable String fileName) {
		if (folder == null) {
			folder = ""; // Set folder to empty string if it's null
		}
		FileInfo fileInfo = new FileInfo(folder, fileName);
		fileService.deleteFile(fileInfo);
		return "redirect:/trang-chu"; // redirect to course page
	}
}
