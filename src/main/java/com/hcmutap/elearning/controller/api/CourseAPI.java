package com.hcmutap.elearning.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.service.ICourseService;
import com.hcmutap.elearning.utils.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class CourseAPI {
    @Autowired
    private ICourseService courseService;
    @PostMapping("/course")
    public void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        CourseModel courseModel = HttpUtil.of(request.getReader()).toModel(CourseModel.class);
        courseService.save(courseModel);
        mapper.writeValue(response.getOutputStream(), courseModel);
    }
    @PutMapping("/course")
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        CourseModel courseModel = HttpUtil.of(request.getReader()).toModel(CourseModel.class);
        courseService.update(courseModel);
        mapper.writeValue(response.getOutputStream(), courseModel);
    }
    @DeleteMapping("/course")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        CourseModel courseModel = HttpUtil.of(request.getReader()).toModel(CourseModel.class);
        courseService.delete(courseModel.getId());
        mapper.writeValue(response.getOutputStream(), "{}");
    }
}
