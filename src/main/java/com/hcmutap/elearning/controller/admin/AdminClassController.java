package com.hcmutap.elearning.controller.admin;

import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.service.IClassService;
import com.hcmutap.elearning.service.ICourseService;
import com.hcmutap.elearning.utils.MapperUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminClassController {
    @Resource
    IClassService classService;
    @GetMapping("/admin-management/addClass")
    public String addClass(ModelMap model) {
        return "admin/views/createClass";
    }
    @PostMapping("/admin-management/addClass")
    public String addClass(@ModelAttribute ClassModel classModel) {
        ModelMap modelMap = MapperUtil.getInstance().toModelMapFromDTO(classModel);
        classService.save(classModel);
        return "redirect:/admin-management?type=class";
    }

}
