package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.repository.ClassRepository;
import com.hcmutap.elearning.repository.document.ClassDocument;
import com.hcmutap.elearning.service.IClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassService implements IClassService{
    @Autowired
    private ClassRepository classRepository;

    @Override
    public List<ClassModel> findAll() {
        List<ClassModel> classModelList = new ArrayList<>();
        List<ClassDocument> classDocumentList = classRepository.findAll();
        for(ClassDocument classDocument:classDocumentList){
            ClassModel classModel = new ClassModel();
            classModel.setId(classDocument.getId());
            classModel.setClassId(classDocument.getClassId());
            classModel.setClassName(classDocument.getClassName());
            classModel.setCourseId(classDocument.getCourseId());
            classModel.setCourseName(classDocument.getCourseName());
            classModel.setTeacherId(classDocument.getTeacherId());
            classModel.setTeacherName(classDocument.getTeacherName());
            classModel.setRoom(classDocument.getRoom());
            classModel.setTime(classDocument.getTime());
            //Set listStudent?
            classModelList.add(classModel);
        }
        return classModelList;
    }

    @Override
    public void save(ClassModel classModel) {
        ClassDocument classDocument = new ClassDocument();
        classDocument.setId(classModel.getId());
        classDocument.setClassId(classModel.getClassId());
        classDocument.setClassName(classModel.getClassName());
        classDocument.setCourseId(classModel.getCourseId());
        classDocument.setCourseName(classModel.getCourseName());
        classDocument.setTeacherId(classModel.getTeacherId());
        classDocument.setTeacherName(classModel.getTeacherName());
        classDocument.setRoom(classModel.getRoom());
        classDocument.setTime(classModel.getTime());
        //Set listStudent?
        classRepository.push(classDocument);
    }

    @Override
    public void update(ClassModel classModel) {
        ClassDocument classDocument = new ClassDocument();
        classDocument.setId(classModel.getId());
        classDocument.setClassId(classModel.getClassId());
        classDocument.setClassName(classModel.getClassName());
        classDocument.setCourseId(classModel.getCourseId());
        classDocument.setCourseName(classModel.getCourseName());
        classDocument.setTeacherId(classModel.getTeacherId());
        classDocument.setTeacherName(classModel.getTeacherName());
        classDocument.setRoom(classModel.getRoom());
        classDocument.setTime(classModel.getTime());
        //Set listStudent?
        classRepository.update(classDocument);
    }

    @Override
    public void delete(String id) {
        classRepository.remove(id);
    }
    @Override
    public List<ClassModel> getClassList (String courseId) {
        List<ClassModel> classModelList = new ArrayList<>();
        List<ClassDocument> classDocumentList = classRepository.findAll();
        for(ClassDocument classDocument:classDocumentList) {
            if(classDocument.getCourseId().equals(courseId)) {
                ClassModel classModel = new ClassModel();
                classModel.setId(classDocument.getId());
                classModel.setClassId(classDocument.getClassId());
                classModel.setClassName(classDocument.getClassName());
                classModel.setCourseId(classDocument.getCourseId());
                classModel.setCourseName(classDocument.getCourseName());
                classModel.setTeacherId(classDocument.getTeacherId());
                classModel.setTeacherName(classDocument.getTeacherName());
                classModel.setRoom(classDocument.getRoom());
                classModel.setTime(classDocument.getTime());
                //Set listStudent?
                classModelList.add(classModel);
            }
        }
        return classModelList;
    }

}
