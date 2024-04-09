package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.Document;
import com.hcmutap.elearning.model.FileInfo;
import com.hcmutap.elearning.model.InfoClassModel;

public interface IInfoService extends IGenericAdminService<InfoClassModel>{
    void delete(String infoId);
    InfoClassModel getClassInfo(String classId);
    boolean updateTile(String idClassInfo, Document docCurrent, String newTitle);
    boolean addFile(String idClassInfo, Document docCurrent, FileInfo file);
    boolean deleteFile(String idClassInfo, Document docCurrent, FileInfo file);
    boolean addNewDoc(String idClassInfo);
    boolean addDoc(String idClassInfo, Document doc);
    boolean deleteDoc(String idClassInfo, Document doc);
}
