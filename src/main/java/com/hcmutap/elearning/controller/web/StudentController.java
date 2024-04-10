package com.hcmutap.elearning.controller.web;


import com.hcmutap.elearning.dao.impl.PointDAO;
import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.service.IPointService;
import com.hcmutap.elearning.service.IClassService;
import com.hcmutap.elearning.service.ICourseService;
import com.hcmutap.elearning.service.IStudentService;
import com.hcmutap.elearning.service.impl.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private IClassService classService;
    @Resource
    private ICourseService courseService;

    @Resource
    private IPointService pointService;

    @RequestMapping("/service")
    public String service(Model model, Principal principal) {
        InfoDTO infoDTO = userService.getInfo(principal.getName());
        if (infoDTO.getRole().equalsIgnoreCase("ADMIN") || infoDTO.getRole().equalsIgnoreCase("TEACHER")) {
            model.addAttribute("message", "You are not a student");
            return "login/404_page";
        }
        model.addAttribute("type", "student");
        return "web/views/student";
    }


//    @GetMapping(value = "/registration")
//    public String regis(Principal principal, ModelMap model){
//        InfoDTO infoDTO = userService.getInfo(principal.getName());
//        StudentModel studentModel = studentService.findById(infoDTO.getId());
//        List<CourseModel> courses = studentService.get_course(studentModel.getId());
//        model.addAttribute("courses", courses);
//        return "web/views/student-service/registration";
//    }

    @GetMapping(value = "/registration")
    public String regis(@RequestParam("courseId") String id, Principal principal, ModelMap model){
        InfoDTO infoDTO = userService.getInfo(principal.getName());
        List<ClassModel> classes = classService.getClassOfCourse(id);
        StudentModel studentModel = studentService.findById(infoDTO.getId());
        List<CourseModel> courses = studentService.get_course(studentModel.getId());
        model.addAttribute("courses", courses);
        model.addAttribute("classes", classes);
        return "web/views/student-service/registration";
    }

    @PostMapping(value = "/registration")
    public String registed(@RequestParam("classId") String classId,Principal principal, ModelMap modelMap){
        InfoDTO infoDTO = userService.getInfo(principal.getName());
        StudentModel studentModel = studentService.findById(infoDTO.getId());
        ClassModel classModel = classService.getClassInfo(classId);

//        boolean con1 = studentService.DangkiMonhoc(studentModel.getId(), classModel.getClassId());
//        if(con1){
//            boolean con2 = studentService.add_class_to_student(studentModel.getId(), classModel.getClassId());
//            if(!con2){
//                modelMap.addAttribute("message", "This class is full");
//            }
//        }
//        else{
//            modelMap.addAttribute("message", "This course is exist");
//        }

        studentModel.getClasses().add(classModel.getClassId());
        studentModel.getCourses().add(classModel.getCourseId());
        studentService.update(studentModel);
        List<CourseModel> courses = studentService.get_course(studentModel.getId());
        modelMap.addAttribute("courses", courses);
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

    @GetMapping(value = "/learning-process")
    public String learningprocess(Principal principal, ModelMap model){
        InfoDTO infoDTO = userService.getInfo(principal.getName());
        StudentModel studentModel = studentService.findById(infoDTO.getId());
        List<CourseModel>courseModels=studentService.get_course(studentModel.getId());
        model.addAttribute("courses", courseModels);
        return "web/views/student-service/learning-process";
    }
}