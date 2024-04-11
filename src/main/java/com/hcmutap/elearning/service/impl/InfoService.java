package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.impl.InfoDAO;
import com.hcmutap.elearning.model.Document;
import com.hcmutap.elearning.model.FileInfo;
import com.hcmutap.elearning.model.InfoClassModel;
import com.hcmutap.elearning.service.IInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoService implements IInfoService {
    private final InfoDAO infoDAO;

    @Autowired
    public InfoService(InfoDAO infoDAO) {
        this.infoDAO = infoDAO;
    }

    @Override
    public List<InfoClassModel>  findAll() {
        return infoDAO.findAll();
    }

    @Override
    public List<InfoClassModel> findBy(String key, String value) {
        return null;
    }

    @Override
    public InfoClassModel findById(String id) {
        return infoDAO.findById(id);
    }

    @Override
    public String save(InfoClassModel infoClass) {
        return infoDAO.save(infoClass);
    }

    @Override
    public void update(InfoClassModel infoClass) {
        infoDAO.update(infoClass);
    }

    @Override
    public void delete(List<String> ids) {
        for(String id : ids)
            infoDAO.delete((id));
    }

    @Override
    public void delete(String infoId) {
        infoDAO.delete(infoId);
    }

    @Override
    public InfoClassModel getClassInfo(String classId) {
        return infoDAO.getClassInfo(classId);
    }

    @Override
    public boolean updateTile(String idClassInfo, Document docCurrent, String newTitle) {
        return infoDAO.updateTile(idClassInfo, docCurrent, newTitle);
    }

    @Override
    public boolean addFile(String idClassInfo, Document docCurrent, FileInfo file) {
        return infoDAO.addFile(idClassInfo, docCurrent, file);
    }

    @Override
    public boolean deleteFile(String idClassInfo, Document docCurrent, FileInfo file) {
        return infoDAO.deleteFile(idClassInfo, docCurrent, file);
    }

    @Override
    public boolean addNewDoc(String idClassInfo) {
        return infoDAO.addDoc(idClassInfo);
    }

    @Override
    public boolean addDoc(String idClassInfo, Document doc) {
        return infoDAO.addDoc(idClassInfo, doc);
    }
    @Override
    public boolean deleteDoc(String idClassInfo, Document doc) {
        return infoDAO.deleteDoc(idClassInfo, doc);
    }
}
