package com.hcmutap.elearning.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.service.ICourseService;
import com.hcmutap.elearning.utils.HttpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseAPI {
    private static final Logger logger = LoggerFactory.getLogger(CourseAPI.class);
    @Resource
    private ICourseService courseService;
    @GetMapping("/courses/findAll")
    public List<CourseModel> findAll() {
        return courseService.findAll();
    }
    @GetMapping("/courses/findById")
    public CourseModel findById(@RequestParam String id) {
        try {
            return courseService.findById(id);
        } catch (NotFoundException e) {
//			throw new RuntimeException(e);
            logger.error(String.valueOf(new RuntimeException(e)));
            return null;
        }
    }
    @PostMapping("/courses")
    public String save(@RequestBody CourseModel courseModel) {
        return courseService.save(courseModel);
    }
    @PutMapping("/courses")
    public void update(@RequestBody CourseModel courseModel) {
        // need to handler not found exception in dao -> service -> controller
        try {
            courseService.update(courseModel);
        } catch (NotFoundException e) {
//			throw new RuntimeException(e);
            logger.error(String.valueOf(new RuntimeException(e)));
        }
    }
    @DeleteMapping("/courses")
    public void delete(@RequestBody List<String> ids) {
        try {
            ids.forEach(id -> courseService.delete(id));
        } catch (Exception e) {
//			throw new RuntimeException(e);
            logger.error(String.valueOf(new RuntimeException(e)));
        }
    }
}
