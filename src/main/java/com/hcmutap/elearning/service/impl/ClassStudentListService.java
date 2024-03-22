package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.model.ClassStudentListModel;
import com.hcmutap.elearning.repository.ClassStudentListRepository;
import com.hcmutap.elearning.repository.document.ClassStudentListDocument;
import com.hcmutap.elearning.service.IClassStudentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassStudentListService implements IClassStudentListService {
    @Autowired
    private ClassStudentListRepository classStudentListRepository;

    @Override
    public List<ClassStudentListModel> findAll() {
        List<ClassStudentListModel> classStudentListModelList = new ArrayList<>();
        List<ClassStudentListDocument> classStudentListDocumentList = classStudentListRepository.findAll();
        for(ClassStudentListDocument classStudentListDocument:classStudentListDocumentList){
            ClassStudentListModel classStudentListModel = new ClassStudentListModel();
            classStudentListModel.setId(classStudentListDocument.getId());
            classStudentListModel.setClassId(classStudentListDocument.getClassId());
            classStudentListModel.setClassName(classStudentListDocument.getClassName());
            classStudentListModel.setStudentId(classStudentListDocument.getStudentId());
            classStudentListModel.setStudentName(classStudentListDocument.getStudentName());
            classStudentListModelList.add(classStudentListModel);
        }
        return classStudentListModelList;
    }

    @Override
    public void save(ClassStudentListModel classStudentListModel) {
        ClassStudentListDocument classStudentListDocument = new ClassStudentListDocument();
        classStudentListDocument.setId(classStudentListModel.getId());
        classStudentListDocument.setClassId(classStudentListModel.getClassId());
        classStudentListDocument.setClassName(classStudentListModel.getClassName());
        classStudentListDocument.setStudentId(classStudentListModel.getStudentId());
        classStudentListDocument.setStudentName(classStudentListModel.getStudentName());
        classStudentListRepository.push(classStudentListDocument);
    }

    @Override
    public void update(ClassStudentListModel classStudentListModel) {
        ClassStudentListDocument classStudentListDocument = new ClassStudentListDocument();
        classStudentListDocument.setId(classStudentListModel.getId());
        classStudentListDocument.setClassId(classStudentListModel.getClassId());
        classStudentListDocument.setClassName(classStudentListModel.getClassName());
        classStudentListDocument.setStudentId(classStudentListModel.getStudentId());
        classStudentListDocument.setStudentName(classStudentListModel.getStudentName());
        classStudentListRepository.update(classStudentListDocument);
    }
    @Override
    public void delete(String id) {
        classStudentListRepository.remove(id);
    }
    @Override
    public List<ClassStudentListModel> findStudentByClassId(String classId) {
        List<ClassStudentListModel> classStudentListModelList = new ArrayList<>();
        List<ClassStudentListDocument> classStudentListDocumentList = classStudentListRepository.findAll();
        for (ClassStudentListDocument classStudentListDocument : classStudentListDocumentList) {
            if (classStudentListDocument.getClassId().equals(classId)) {
                ClassStudentListModel classStudentListModel = new ClassStudentListModel();
                classStudentListModel.setId(classStudentListDocument.getId());
                classStudentListModel.setClassId(classStudentListDocument.getClassId());
                classStudentListModel.setClassName(classStudentListDocument.getClassName());
                classStudentListModel.setStudentId(classStudentListDocument.getStudentId());
                classStudentListModel.setStudentName(classStudentListDocument.getStudentName());
                classStudentListModelList.add(classStudentListModel);
            }
        }
        return classStudentListModelList;
    }
}
