package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.CourseDAO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.exception.NotFoundInDB;
import com.hcmutap.elearning.model.CourseModel;
import com.hcmutap.elearning.model.ClassModel;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.IClassService;
import com.hcmutap.elearning.service.ICourseService;
import com.hcmutap.elearning.service.IPointService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService implements ICourseService {
    public CourseService() {
        courseDAO = new CourseDAO();
    }
    @Resource
    private CourseDAO courseDAO;
    @Resource
    private IClassService classService;
    @Resource
    private IPointService pointService;

    @Override
    public List<CourseModel> findAll() {
        return courseDAO.findAll();
    }

    @Override
    public List<CourseModel> findBy(String key, String value) {
        return null;
    }

    @Override
    public CourseModel findById(String courseId) throws NotFoundException {
		try {
			return courseDAO.findById(courseId);
		} catch (NotFoundInDB e) {
			throw new NotFoundException(e.getMessage());
		}
	}

    @Override
    public String save(CourseModel courseModel) {
        return courseDAO.save(courseModel);
    }

    @Override
    public void update(CourseModel courseModel) {
        courseDAO.update(courseModel);
    }

    @Override
    public void delete(List<String> ids) {

    }

    @Override
    public void delete(String courseId) {
        courseDAO.delete(courseId);
    }

    @Override
    public CourseModel getCourseInfo(String courseId) throws NotFoundException {
		try {
			return this.findById(courseId);
		} catch (NotFoundException e) {
			throw new NotFoundException(e.getMessage());
		}
	}

    @Override
    public List<ClassModel> getLichTrinh(String courseId) throws NotFoundException {
        return classService.getClassOfCourse(courseId);
    }

    @Override
    public List<PointModel> getListPointOfStudent(String courseId) throws NotFoundException {
        return pointService.getListStudentByCourseId(courseId);
    }
    @Override
    public boolean isExist(String id) {
        return !courseDAO.findBy("courseId", id, Options.OptionBuilder.Builder().setEqual().build()).isEmpty();
    }

    @Override
    public Page<CourseModel> getPage(String key, int page, int size) {
        return courseDAO.search(key, PageRequest.of(page - 1, size));
    }
}
