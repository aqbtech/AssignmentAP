package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.model.Document;
import com.hcmutap.elearning.model.FileInfo;
import com.hcmutap.elearning.model.InfoClassModel;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class InfoDAO extends DefaultFirebaseDatabase<InfoClassModel, String>{
    public InfoClassModel getClassInfo(String classId) {
        return findBy("classId", classId, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
    }

    public boolean updateTile(String idClassInfo, Document docCurrent, String newTitle) {
        InfoClassModel info;
        try{
            info = findById(idClassInfo);
        } catch (Exception e){
            return false;
        }
        for(Document doc : info.getListDocument()) {
            if(docCurrent.getTitle().equals(doc.getTitle())) {
                doc.setTitle(newTitle);
                update(info);
                return true;
            }
        }
        return false;
    }

    public boolean addFile(String idClassInfo, Document docCurrent, FileInfo file) {
        InfoClassModel info;
        try{
            info = findById(idClassInfo);
        } catch (Exception e){
            return false;
        }
        for(Document doc : info.getListDocument()) {
            if(docCurrent.getTitle().equals(doc.getTitle())) {
                doc.getListFile().add(file);
                update(info);
                return true;
            }
        }
        return false;
    }

    public boolean deleteFile(String idClassInfo, Document docCurrent, FileInfo file) {
        InfoClassModel info;
        try{
            info = findById(idClassInfo);
        } catch (Exception e){
            return false;
        }
        List<Document> ListDoc = info.getListDocument();
        for(Document doc : ListDoc) {
            if(docCurrent.getTitle().equals(doc.getTitle())) {
                List<FileInfo> ListFile = doc.getListFile();
                ListFile.removeIf(fileInfo -> file.getFileName().equals(fileInfo.getFileName()));
                doc.setListFile(ListFile);
                info.setListDocument(ListDoc);
                update(info);
                return true;
            }
        }
        return false;
    }

    public boolean addDoc(String idClassInfo) {
        InfoClassModel info;
        try{
            info = findById(idClassInfo);
        } catch (Exception e){
            return false;
        }
        info.getListDocument().add(new Document());
        update(info);
        return true;
    }
    public boolean addDoc (String idClassInfo, Document doc) {
        InfoClassModel info;
        try{
            info = findById(idClassInfo);
        } catch (Exception e){
            return false;
        }
        info.getListDocument().add(doc);
        update(info);
        return true;
    }

    public boolean deleteDoc (String idClassInfo, Document doc) {
        InfoClassModel info;
        try{
            info = findById(idClassInfo);
        } catch (Exception e){
            return false;
        }

        List<Document> newListDoc = info.getListDocument();
        newListDoc.removeIf(document -> doc.getTitle().equals(document.getTitle()));
        info.setListDocument(newListDoc);
        update(info);
        return true;
    }
}
