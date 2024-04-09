package com.hcmutap.elearning.model;

import com.hcmutap.elearning.dao.firebase.DocumentId;
import com.hcmutap.elearning.dao.firebase.FirebaseCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@FirebaseCollection("infoClasses")
public class InfoClassModel {
    @DocumentId
    private String id;
    private String classId;
    private String className;
    private List<Document> listDocument;

    public InfoClassModel() {
        long timestamp = System.currentTimeMillis();
        String id = String.valueOf(timestamp);
        this.id = id;
        classId = null;
        className = null;
        listDocument = new ArrayList<>();
    }
}
