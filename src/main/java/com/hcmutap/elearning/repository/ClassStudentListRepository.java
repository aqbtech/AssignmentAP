package com.hcmutap.elearning.repository;

import com.github.fabiomaffioletti.firebase.repository.DefaultFirebaseRealtimeDatabaseRepository;
import com.hcmutap.elearning.repository.document.ClassStudentListDocument;
import org.springframework.stereotype.Repository;

@Repository
public class ClassStudentListRepository extends DefaultFirebaseRealtimeDatabaseRepository<ClassStudentListDocument, String> {
}
