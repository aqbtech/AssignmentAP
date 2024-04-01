package com.hcmutap.elearning.controller.web;

import com.hcmutap.elearning.dao.impl.CourseDAO;
import com.hcmutap.elearning.dao.impl.PointDAO;
import com.hcmutap.elearning.dto.InfoDTO;
import com.hcmutap.elearning.dto.PointDTO;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.service.ICourseService;
import com.hcmutap.elearning.service.IPointService;
import com.hcmutap.elearning.service.IStudentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class PointController {
    @Autowired
    IPointService pointService;
    @Autowired
    IStudentService studentService;
    @Autowired
    PointDAO pointDAO;
    @Autowired
    CourseDAO courseDAO;
    @Autowired
    ICourseService courseService;

    @RequestMapping("/point/view")
    public String printPoint(ModelMap model, @RequestParam String studentID){
        List<PointModel> pointModelList = pointService.getListPointOfStudent(studentID);
        List<CourseModel> courseModelList = new ArrayList<>();
        ArrayList<Double> resultAverageList = new ArrayList<>();
        for (PointModel pointModel : pointModelList){
            courseModelList.add(courseService.getCourseInfo(pointModel.getCourseId()));
            resultAverageList.add(pointService.getAveragePoint(pointModel.getStudentId(),pointModel.getCourseId()));
        }
        model.addAttribute("results",resultAverageList);
        model.addAttribute("courses",courseModelList);
        model.addAttribute("points",pointModelList);
        return "viewPoint";
    }

    @RequestMapping("/point/create")
    public String create(ModelMap model){
        for(int i=0;i<5;i++){
//            PointModel pointModel = new PointModel(toString(i),"999","999",toString(i),toString(i),true,i+0.1,i+0.1,i+0.1,i+0.1);
//            pointService.save(pointModel);

            CourseModel courseModel = new CourseModel(toString(i),toString(i),i,i,i,i,i);
            courseService.save(courseModel);
        }
//        for (int i=0;i<5;i++){
////           pointDAO.delete(toString(i));
////            courseDAO.delete(null);
//        }
//        courseDAO.delete("234");
//        courseDAO.delete("123");
//        courseDAO.delete("111");

        return "test1";
    }

    private String toString(int i) {
        if(i==0) return "0";
        if(i==1) return "1";
        if(i==2) return "2";
        if(i==3) return "3";
        if(i==4) return "4";
        if(i==5) return "5";
        return null;
    }
}
