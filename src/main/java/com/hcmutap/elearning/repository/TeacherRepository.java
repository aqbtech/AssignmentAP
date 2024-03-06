package com.hcmutap.elearning.repository;

import com.github.fabiomaffioletti.firebase.repository.DefaultFirebaseRealtimeDatabaseRepository;
import com.hcmutap.elearning.repository.document.TeacherDocument;
import org.springframework.stereotype.Repository;

@Repository
public class TeacherRepository extends DefaultFirebaseRealtimeDatabaseRepository<TeacherDocument, String> {
}
