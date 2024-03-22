package com.hcmutap.elearning.repository;

import com.github.fabiomaffioletti.firebase.repository.DefaultFirebaseRealtimeDatabaseRepository;
import com.hcmutap.elearning.repository.document.ClassDocument;
import org.springframework.stereotype.Repository;

@Repository
public class ClassRepository extends DefaultFirebaseRealtimeDatabaseRepository<ClassDocument, String> {

}
