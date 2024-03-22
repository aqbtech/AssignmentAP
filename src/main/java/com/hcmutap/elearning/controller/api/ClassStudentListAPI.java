package com.hcmutap.elearning.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmutap.elearning.model.ClassStudentListModel;
import com.hcmutap.elearning.service.IClassStudentListService;
import com.hcmutap.elearning.utils.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ClassStudentListAPI {
    @Autowired
    private IClassStudentListService classStudentListService;
    @PostMapping("/class-student-list")
    public void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ClassStudentListModel classStudentListModel = HttpUtil.of(request.getReader()).toModel(ClassStudentListModel.class);
        classStudentListService.save(classStudentListModel);
        mapper.writeValue(response.getOutputStream(), classStudentListModel);
    }
    @PutMapping("/class-student-list")
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ClassStudentListModel classStudentListModel = HttpUtil.of(request.getReader()).toModel(ClassStudentListModel.class);
        classStudentListService.update(classStudentListModel);
        mapper.writeValue(response.getOutputStream(), classStudentListModel);
    }
    @DeleteMapping("/class-student-list")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ClassStudentListModel classStudentListModel = HttpUtil.of(request.getReader()).toModel(ClassStudentListModel.class);
        classStudentListService.delete(classStudentListModel.getId());
        mapper.writeValue(response.getOutputStream(), "{}");
    }
}
