package com.hcmutap.elearning.controller.api;

import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.service.IClassService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClassAPI {
    @Resource
    private IClassService classService;
    @GetMapping("/class/findAll")
    public List<ClassModel> findAll() {
        return classService.findAll();
    }
    @GetMapping("/class/findById")
    public ClassModel findById(@RequestParam String id) {
        return classService.findById(id);
    }
    @PostMapping("/class")
    public String save(@RequestBody ClassModel classModel) { return classService.save(classModel);}
    @PutMapping("/class")
    public void update(@RequestBody ClassModel classModel) {
        // need to handler not found exception in dao -> service -> controller
        classService.update(classModel);
    }
    @DeleteMapping("/class")
    public void delete(@RequestBody List<String> ids) {
        ids.forEach(id -> classService.delete(id));
    }
}
