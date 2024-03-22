package com.hcmutap.elearning.repository;

import com.github.fabiomaffioletti.firebase.repository.DefaultFirebaseRealtimeDatabaseRepository;
import com.hcmutap.elearning.repository.document.CourseDocument;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepository extends DefaultFirebaseRealtimeDatabaseRepository<CourseDocument, String> {

}
