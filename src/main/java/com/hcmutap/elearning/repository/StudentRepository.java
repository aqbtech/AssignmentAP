package com.hcmutap.elearning.repository;

import com.github.fabiomaffioletti.firebase.repository.DefaultFirebaseRealtimeDatabaseRepository;
import com.hcmutap.elearning.repository.document.StudentDocument;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepository extends DefaultFirebaseRealtimeDatabaseRepository<StudentDocument, String> {

}
