package com.hcmutap.elearning.service;

import com.hcmutap.elearning.exception.TransactionalException;
import com.hcmutap.elearning.model.Document;
import com.hcmutap.elearning.model.FileInfo;
import com.hcmutap.elearning.model.InfoClassModel;

public interface IInfoService extends IGenericAdminService<InfoClassModel>{
    void delete(String infoId) throws TransactionalException;
    InfoClassModel getClassInfo(String classId);
    boolean updateTile(String idClassInfo, Document docCurrent, String newTitle) throws TransactionalException;
    boolean addFile(String idClassInfo, Document docCurrent, FileInfo file) throws TransactionalException;
    boolean deleteFile(String idClassInfo, Document docCurrent, FileInfo file) throws TransactionalException;
    boolean addNewDoc(String idClassInfo) throws TransactionalException;
    boolean addDoc(String idClassInfo, Document doc) throws TransactionalException;
    boolean deleteDoc(String idClassInfo, Document doc) throws TransactionalException;
}
