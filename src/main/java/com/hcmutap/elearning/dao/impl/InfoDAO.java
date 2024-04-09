package com.hcmutap.elearning.dao.impl;

import com.hcmutap.elearning.dao.firebase.DefaultFirebaseDatabase;
import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.model.Document;
import com.hcmutap.elearning.model.FileInfo;
import com.hcmutap.elearning.model.InfoClassModel;
import org.springframework.stereotype.Repository;


@Repository
public class InfoDAO extends DefaultFirebaseDatabase<InfoClassModel, String>{
    public InfoClassModel findById(String id) {
        return findBy("id", id, Options.OptionBuilder.Builder().setEqual().build()).getFirst();
    }

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
            if(docCurrent.equals(doc)) {
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
            if(docCurrent.equals(doc)) {
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
        for(Document doc : info.getListDocument()) {
            if(docCurrent.equals(doc)) {
                boolean check = doc.getListFile().remove(file);
                if(check)
                {
                    update(info);
                    return true;
                } else
                    return false;
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
        boolean check = info.getListDocument().remove(doc);
        if(check) {
            update(info);
            return true;
        } else
            return false;
    }
}
