package com.hcmutap.elearning.controller.web;


import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.service.IStudentService;
import com.hcmutap.elearning.service.ITeacherService;
import com.hcmutap.elearning.service.impl.CourseFacade;
import com.hcmutap.elearning.service.impl.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/student")
public class StudentController{
    @Resource
    private UserService userService;
    @Resource
    private IStudentService studentService;

    @RequestMapping("/service")
    public String service(){return "web/views/student";}

    @GetMapping(value = "/registration")
    public String regis(Principal principal,ModelMap model){
        InfoDTO infoDTO = userService.getInfo(principal.getName());
        StudentModel studentModel = studentService.findById(infoDTO.getId());
        List<CourseModel> courses = studentService.get_course(studentModel.getId());
        model.addAttribute("courses", courses);
        return "web/views/student-service/registration";
    }
    @GetMapping(value = "/timetable")
    public String timetable(Principal principal,ModelMap model){
        InfoDTO infoDTO = userService.getInfo(principal.getName());
        StudentModel studentModel = studentService.findById(infoDTO.getId());
        List<ClassModel> classes = studentService.get_timetable(studentModel.getId());
        model.addAttribute("classes", classes);
        return "web/views/student-service/time-table";
    }

}