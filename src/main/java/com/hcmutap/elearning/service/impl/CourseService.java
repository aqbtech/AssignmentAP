package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.repository.CourseRepository;
import com.hcmutap.elearning.repository.document.CourseDocument;
import com.hcmutap.elearning.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService implements ICourseService {
    @Autowired
    private CourseRepository courseRepository;
    private CourseModel getCourseModel(CourseDocument courseDocument) {
        CourseModel courseModel = new CourseModel();
        courseModel.setId(courseDocument.getId());
        courseModel.setCourseId(courseDocument.getCourseId());
        courseModel.setCourseName(courseDocument.getCourseName());
        courseModel.setCredit(courseDocument.getCredit());
        courseModel.setDescription(courseDocument.getDescription());
        return courseModel;
    }
    private CourseDocument getCourseDocument(CourseModel courseModel) {
        CourseDocument courseDocument = new CourseDocument();
        courseDocument.setId(courseModel.getId());
        courseDocument.setCourseId(courseModel.getCourseId());
        courseDocument.setCourseName(courseModel.getCourseName());
        courseDocument.setCredit(courseModel.getCredit());
        courseDocument.setDescription(courseModel.getDescription());
        return courseDocument;
    }

    @Override
    public List<CourseModel> findAll() {
        List<CourseModel> courseModelList = new ArrayList<>();
        List<CourseDocument> courseDocumentList = courseRepository.findAll();
        for (CourseDocument courseDocument : courseDocumentList) {
            CourseModel courseModel = getCourseModel(courseDocument);
            courseModelList.add(courseModel);
        }
        return courseModelList;
    }
    @Override
    public void save(CourseModel courseModel) {
        CourseDocument courseDocument = getCourseDocument(courseModel);
        courseRepository.push(courseDocument);
    }

    @Override
    public void update(CourseModel courseModel) {
        CourseDocument courseDocument = getCourseDocument(courseModel);
        courseRepository.update(courseDocument);
    }

    @Override
    public void delete(String id) {
        courseRepository.remove(id);
    }

}
