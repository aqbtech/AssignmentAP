package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.SemesterModel;
import org.springframework.data.domain.Page;

public interface ISemesterService extends IGenericAdminService<SemesterModel> {
    SemesterModel getSemeter(String semesterId);
	Page<SemesterModel> getPage(String keyword, int page, int size);
}
