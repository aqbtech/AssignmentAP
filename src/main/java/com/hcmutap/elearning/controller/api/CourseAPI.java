package com.hcmutap.elearning.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.service.ICourseService;
import com.hcmutap.elearning.utils.HttpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseAPI {
    @Resource
    private ICourseService courseService;
    @GetMapping("/courses/findAll")
    public List<CourseModel> findAll() {
        return courseService.findAll();
    }
    @GetMapping("/courses/findById")
    public CourseModel findById(@RequestParam String id) {
        return courseService.findById(id);
    }
    @PostMapping("/courses")
    public String save(@RequestBody CourseModel courseModel) {
        return courseService.save(courseModel);
    }
    @PutMapping("/courses")
    public void update(@RequestBody CourseModel courseModel) {
        // need to handler not found exception in dao -> service -> controller
        courseService.update(courseModel);
    }
    @DeleteMapping("/courses")
    public void delete(@RequestBody List<String> ids) {
        ids.forEach(id -> courseService.delete(id));
    }
}
