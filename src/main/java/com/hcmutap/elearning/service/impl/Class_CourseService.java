package com.hcmutap.elearning.service.impl;


import com.hcmutap.elearning.dto.Class_CourseDTO;
import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.exception.MappingException;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.*;
import com.hcmutap.elearning.service.IClass_CourseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Class_CourseService implements IClass_CourseService {
    @Resource
    private StudentService studentService;
    @Resource
    private TeacherService teacherService;
    @Resource
    private UserService userService;
    @Resource
    private CourseService courseService;
    @Resource
    private ClassService classService;

    @Override
    public List<Class_CourseDTO> getClass_Course(String Username) throws NotFoundException, MappingException {
        InfoDTO infoDTO = userService.getInfo(Username);
        if(infoDTO.getRole().equals("teacher")){
            TeacherModel teacherModel = teacherService.findById(infoDTO.getId());
            List<Class_CourseDTO> result = new ArrayList<>();
            List<ClassModel> classes = teacherService.getAllClass(teacherModel.getUsername());
            List<CourseModel> courses = teacherService.get_course(teacherModel.getId());
            return getClassCourseDTOS(result, classes, courses);
        }
        if(infoDTO.getRole().equals("student")){
            StudentModel studentModel = studentService.findById(infoDTO.getId());
            List<Class_CourseDTO> result = new ArrayList<>();
            List<ClassModel> classes = studentService.getAllClass(studentModel.getUsername());
            List<CourseModel> courses = studentService.getCourseByIf(studentModel.getId());
            return getClassCourseDTOS(result, classes, courses);
        }
        return List.of();
    }

    private List<Class_CourseDTO> getClassCourseDTOS(List<Class_CourseDTO> result, List<ClassModel> classes, List<CourseModel> courses) {
        if(classes.isEmpty()) return result;
        for(int i = 0; i < classes.size(); i ++){
            Class_CourseDTO  e = new Class_CourseDTO(classes.get(i), courses.get(i));
            result.add(e);
        }
        return result;
    }

    @Override
    public List<Class_CourseDTO> getByCourseId(String courseId){
        if(!courseService.isExist(courseId) || courseId.isEmpty()){
            return null;
        }
        try {
            CourseModel course = courseService.getCourseInfo(courseId);
            List<ClassModel> classes = classService.getClassOfCourse(courseId);
            List<Class_CourseDTO> result = new ArrayList<>();
            for (int i = 0; i < classes.size(); i++) {
                Class_CourseDTO e = new Class_CourseDTO(classes.get(i), course);
                result.add(e);
            }
            return result;
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkClass_Course(List<Class_CourseDTO> list_e){
        for (Class_CourseDTO e : list_e){
            if(e.getLophoc() != null && e.getCourse() == null){
                return false;
            }
        }
        return true;
    }

}
