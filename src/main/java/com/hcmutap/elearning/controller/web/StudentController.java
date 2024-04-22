package com.hcmutap.elearning.controller.web;


import com.hcmutap.elearning.Singleton;
import com.hcmutap.elearning.dto.Class_CourseDTO;
import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.model.*;
import com.hcmutap.elearning.service.*;
import com.hcmutap.elearning.service.impl.Class_CourseService;
import com.hcmutap.elearning.service.impl.UserService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

@Controller
@RequestMapping(value = "/student")
public class StudentController{
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    @Resource
    private UserService userService;
    @Resource
    private IStudentService studentService;
    @Resource
    private IClassService classService;
    @Resource
    private ICourseService courseService;
    @Resource
    private ISemesterService semesterService;
    @Resource
    private Class_CourseService class_courseService;

    @Resource
    private IPointService pointService;

//    @RequestMapping("/service")
//    public String service(Model model, Principal principal) {
//        InfoDTO infoDTO = userService.getInfo(principal.getName());
//        if (infoDTO.getRole().equalsIgnoreCase("ADMIN") || infoDTO.getRole().equalsIgnoreCase("TEACHER")) {
//            model.addAttribute("message", "You are not a student");
//            return "login/404_page";
//        }
//        model.addAttribute("type", "student");
//        return "web/views/student";
//    }


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
        try {
            Singleton check = Singleton.getInstance();

            InfoDTO infoDTO = userService.getInfo(principal.getName());
            StudentModel studentModel = studentService.findById(infoDTO.getId());
            List<Class_CourseDTO> class_course = new ArrayList<>();
            List<Class_CourseDTO> class_course_of_student = class_courseService.getClass_Course(studentModel.getUsername());
            if(!check.isStudent_state()){
                model.addAttribute("class_course_of_student", class_course_of_student);
                model.addAttribute("class_course", class_course);
                model.addAttribute("message", "Hiện không được đăng ký");
                return "web/views/student-service/registration";
            }
            if(!class_courseService.checkClass_Course(class_course_of_student)){
                return "login/Rare_fault";
            }

            if(id.isEmpty()){
                model.addAttribute("class_course_of_student", class_course_of_student);
                model.addAttribute("class_course", class_course);
                return "web/views/student-service/registration";
            }
            if (courseService.isExist(id)) {
                class_course = class_courseService.getByCourseId(id);
                model.addAttribute("class_course_of_student", class_course_of_student);
                model.addAttribute("class_course", class_course);
                return "web/views/student-service/registration";
            }
            model.addAttribute("message", "Khong co lop hoc ma ban tim kiem");
            model.addAttribute("class_course_of_student", class_course_of_student);
            model.addAttribute("class_course", class_course);
            return "web/views/student-service/registration";
        } catch (Exception e) {
            model.addAttribute("message", "User not found");
            return "login/404_page";
        }
    }

    @PostMapping(value = "/registration")
    public String registed(@RequestParam("classId") String classId,Principal principal, ModelMap modelMap,
                           final RedirectAttributes redirectAttributes){
        try {
            InfoDTO infoDTO = userService.getInfo(principal.getName());
            StudentModel studentModel = studentService.findById(infoDTO.getId());
            String message = studentService.DangkiMonhoc(studentModel.getId(), classId);

            List<Class_CourseDTO> class_course = new ArrayList<>();
            List<Class_CourseDTO> class_course_of_student = new ArrayList<>();

            class_course = class_courseService.getByCourseId(classId);
            class_course_of_student = class_courseService.getClass_Course(studentModel.getUsername());

            modelMap.addAttribute("class_course_of_student", class_course_of_student);
            redirectAttributes.addFlashAttribute("message", message);
            modelMap.addAttribute("class_course", class_course);
            return "redirect:/student/registration?courseId=";
        } catch (Exception e) {
            modelMap.addAttribute("message", "User not found");
            return "login/404_page";
        }
    }
    @GetMapping(value = "/timetable")
    public String timetable(Principal principal,ModelMap model){
        try {
            InfoDTO infoDTO = userService.getInfo(principal.getName());
            StudentModel studentModel = studentService.findById(infoDTO.getId());
            List<ClassModel> classes = studentService.getTimetableById(studentModel.getId());
            model.addAttribute("classes", classes);
            return "web/views/student-service/time-table";
        } catch (Exception e) {
            model.addAttribute("message", "User not found");
            return "login/404_page";
        }
    }
    @GetMapping(value = "/score")
    public String score(Principal principal, ModelMap model){
        try {
            InfoDTO infoDTO = userService.getInfo(principal.getName());
            StudentModel studentModel = studentService.findById(infoDTO.getId());

            List<PointModel> points = studentService.getPointById(studentModel.getId());
            ArrayList<Double> resultAverageList = new ArrayList<>();
            for (PointModel pointModel : points) {
                resultAverageList.add(pointService.getAveragePoint(pointModel.getStudentId(), pointModel.getCourseId()));
            }

            List<SemesterModel> semesterModels = new ArrayList<>();
            List<CourseModel> courseModels = new ArrayList<>();

            List<String> classIdlist = studentModel.getClasses();
            for(String s : classIdlist){
                ClassModel classModel = classService.getClassInfo(s);
                courseModels.add(courseService.getCourseInfo(classModel.getCourseId()));
                SemesterModel semesterModell = semesterService.getSemeter(classModel.getSemesterId());
                boolean condition = true;
                for (SemesterModel semesterModel : semesterModels) {
                    if (Objects.equals(semesterModell.getSemesterName(), semesterModel.getSemesterName())) {
                        condition = false;
                    }
                }
                if(condition){
                    semesterModels.add(semesterModell);
                }
            }
            model.addAttribute("semesters", semesterModels);
            model.addAttribute("results", resultAverageList);
            model.addAttribute("points", points);
            model.addAttribute("courses", courseModels);
            return "web/views/student-service/score";
        } catch (Exception e) {
            model.addAttribute("message", "User not found");
            return "login/404_page";
        }
    }

    @GetMapping(value = "/learning-process")
    public String learning_process(Principal principal, ModelMap model){
        try {
            InfoDTO infoDTO = userService.getInfo(principal.getName());
            StudentModel studentModel = studentService.findById(infoDTO.getId());
            List<CourseModel> courseModels = studentService.getCourseByIf(studentModel.getId());
            List<PointModel> courseModels1 = studentService.LearningProcess(studentModel.getId());
            List<SemesterModel> semesterModels = semesterService.findAll();
            model.addAttribute("semesters", semesterModels);
            model.addAttribute("courses", courseModels);
            model.addAttribute("courses1", courseModels1);
            return "web/views/student-service/learning-process";
        } catch (Exception e) {
            model.addAttribute("message", "User not found");
            return "login/404_page";
        }
    }
}