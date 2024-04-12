package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.impl.StudentDAO;
import com.hcmutap.elearning.dto.Class_CourseDTO;
import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.model.*;
import com.hcmutap.elearning.service.IClass_CourseService;
import com.hcmutap.elearning.service.ICourseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Class_CourseService implements IClass_CourseService {
    @Resource
    StudentService studentService;
    @Resource
    TeacherService teacherService;
    @Resource
    UserService userService;
    @Resource
    CourseService courseService;
    @Resource
    ClassService classService;

    @Override
    public List<Class_CourseDTO> getClass_Course(String Username){
        InfoDTO infoDTO = userService.getInfo(Username);
        if(infoDTO.getRole().equals("teacher")){
            TeacherModel teacherModel = teacherService.findById(infoDTO.getId());
            List<Class_CourseDTO> result = new ArrayList<>();
            List<ClassModel> classes = teacherService.getAllClass(teacherModel.getUsername());
            List<CourseModel> courses = teacherService.get_course(teacherModel.getId());
            if(classes.isEmpty()) return result;
            for(int i = 0; i < classes.size(); i ++){
                Class_CourseDTO  e = new Class_CourseDTO(classes.get(i), courses.get(i));
                result.add(e);
            }
            return result;
        }
        if(infoDTO.getRole().equals("student")){
            StudentModel studentModel = studentService.findById(infoDTO.getId());
            List<Class_CourseDTO> result = new ArrayList<>();
            List<ClassModel> classes = studentService.getAllClass(studentModel.getUsername());
            List<CourseModel> courses = studentService.get_course(studentModel.getId());
            if(classes.isEmpty()) return result;
            for(int i = 0; i < classes.size(); i ++){
                Class_CourseDTO  e = new Class_CourseDTO(classes.get(i), courses.get(i));
                result.add(e);
            }
            return result;
        }
        return List.of();
    }

    @Override
    public List<Class_CourseDTO> getByCourseId(String courseId){
        if(!courseService.isExist(courseId) || courseId.isEmpty()){
            return null;
        }
        CourseModel course = courseService.getCourseInfo(courseId);
        List<ClassModel> classes = classService.getClassOfCourse(courseId);
        List<Class_CourseDTO> result = new ArrayList<>();
        for(int i = 0; i < classes.size(); i ++){
            Class_CourseDTO  e = new Class_CourseDTO(classes.get(i), course);
            result.add(e);
        }
        return result;
    }
}
