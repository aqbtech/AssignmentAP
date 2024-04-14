package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.SemesterModel;

public interface ISemesterService extends IGenericAdminService<SemesterModel> {
    SemesterModel getSemeter(String semesterId);
}
