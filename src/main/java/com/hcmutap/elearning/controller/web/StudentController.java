package com.hcmutap.elearning.controller.web;


import com.hcmutap.elearning.dao.impl.PointDAO;
import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.service.IPointService;
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
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/student")
public class StudentController{
    @Resource
    private UserService userService;
    @Resource
    private IStudentService studentService;

    @Resource
    private IPointService pointService;

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
    @GetMapping(value = "/score")
    public String score(Principal principal, ModelMap model){
        InfoDTO infoDTO = userService.getInfo(principal.getName());
        StudentModel studentModel = studentService.findById(infoDTO.getId());
        List<PointModel> points = studentService.get_point(studentModel.getId());
        ArrayList<Double> resultAverageList = new ArrayList<>();
        for (PointModel pointModel : points){
            resultAverageList.add(pointService.getAveragePoint(pointModel.getStudentId(),pointModel.getCourseId()));
        }
        model.addAttribute("results",resultAverageList);
        model.addAttribute("points", points);
        return "web/views/student-service/score";
    }
}