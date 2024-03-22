package com.hcmutap.elearning.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.service.IClassService;
import com.hcmutap.elearning.utils.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ClassAPI {
    @Autowired
    private IClassService classService;
    @PostMapping("/class")
    public void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ClassModel classModel = HttpUtil.of(request.getReader()).toModel(ClassModel.class);
        classService.save(classModel);
        mapper.writeValue(response.getOutputStream(), classModel);
    }
    @PutMapping("/class")
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ClassModel classModel = HttpUtil.of(request.getReader()).toModel(ClassModel.class);
        classService.update(classModel);
        mapper.writeValue(response.getOutputStream(), classModel);
    }
    @DeleteMapping("/class")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ClassModel classModel = HttpUtil.of(request.getReader()).toModel(ClassModel.class);
        classService.delete(classModel.getId());
        mapper.writeValue(response.getOutputStream(), "{}");
    }
}
