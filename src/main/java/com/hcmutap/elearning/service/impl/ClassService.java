package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.dao.impl.*;
import com.hcmutap.elearning.dto.PointDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.exception.NotFoundInDB;
import com.hcmutap.elearning.model.*;
import com.hcmutap.elearning.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassService implements IClassService {
    private final CourseDAO courseDAO;
    private final StudentDAO studentDAO;
    private final PointDAO pointDAO;
    private final ClassDAO classDAO;
    private final TeacherDAO teacherDAO;

    @Autowired
    public ClassService(CourseDAO courseDAO, StudentDAO studentDAO, PointDAO pointDAO, ClassDAO classDAO, TeacherDAO teacherDAO) {
        this.courseDAO = courseDAO;
        this.studentDAO = studentDAO;
        this.pointDAO = pointDAO;
        this.classDAO = classDAO;
        this.teacherDAO = teacherDAO;
    }
    private IPointService pointService;
    private IInfoService infoService;
    @Autowired
    public void setPointService(IPointService pointService) {
        this.pointService = pointService;
    }
    @Autowired
    public void setInfoService(IInfoService infoService) {
        this.infoService = infoService;
    }
    @Override
    public List<ClassModel> findAll() {
        return classDAO.findAll();
    }

    @Override
    public List<ClassModel> findBy(String key, String value) {
        return classDAO.findBy(key, value);
    }

    @Override
    public ClassModel findById(String id) throws NotFoundException {
		try {
			return classDAO.findById(id);
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
	}
    @Override
    public String save(ClassModel classModel) {
        InfoClassModel info = new InfoClassModel();
        long timestamp = System.currentTimeMillis();
        String id = String.valueOf(timestamp);
        info.setId(id);
        info.setClassId(classModel.getClassId());
        info.setClassName(classModel.getClassName());
        info.setListDocument(new ArrayList<>());
        classModel.setInfoId(info.getId());
        infoService.save(info);
        return classDAO.save(classModel);
    }
    @Override
    public void update(ClassModel classModel) {
        classDAO.update(classModel);
    }

    @Override
    public void delete(List<String> ids) {

    }
    @Override
    public void delete(String id) throws NotFoundException {
        List<ClassModel> cls = findBy("firebaseId", id);
        if (!cls.isEmpty()) {
            ClassModel classModel = cls.getFirst();
            InfoClassModel infoClass = infoService.findById(classModel.getInfoId());
            infoService.delete(infoClass.getFirebaseId());
            classDAO.delete(id);
        }
    }

    @Override
    public ClassModel getClassInfo(String classId) throws NotFoundException {
		try {
			return classDAO.getClassInfo(classId);
		} catch (NotFoundInDB e) {
			throw new NotFoundException(e.toString());
		}
	}
    @Override
    public List<ClassModel> getClassOfCourse(String courseId) {
        return classDAO.getClassOfCourse(courseId);
    }
    @Override
    public List<PointModel> getListStudentOfClass(String classId) throws NotFoundException {
        return pointService.getListStudentByClassId(classId);
    }
    @Override
    public List<ClassModel> getTimeTableSV(String studentId) throws NotFoundException {
        return classDAO.getTimeTableSV(studentId);
    }
    @Override
    public List<ClassModel> getTimeTableGV(String teacherId){
        return classDAO.getTimeTableGV(teacherId);
    }

    @Override
    public boolean addStudentToClass(String studentId, String classId) throws NotFoundException {
        try {
            List<String> listClass = studentDAO.findBy("id", studentId).getFirst().getClasses();
            for (String item : listClass) {
                if (item.equals(classId)) {
                    return false;
                }
            }
            long timestamp = System.currentTimeMillis();
            String id = String.valueOf(timestamp);
            StudentModel studentModel = studentDAO.findById(studentId);
            ClassModel classModel = classDAO.getClassInfo(classId);
            CourseModel courseModel = courseDAO.findById(classModel.getCourseId());
            // state = true is learned
            PointModel tmp = new PointModel();
            tmp.setId(id);
            tmp.setStudentId(studentId);
            tmp.setStudentName(studentModel.getFullName());
            tmp.setCourseId(classModel.getCourseId());
            tmp.setCourseName(courseModel.getCourseName());
            tmp.setClassId(classId);
            tmp.setClassName(classModel.getClassName());
            tmp.setSemesterId(classModel.getSemesterId());
            tmp.setState(false);
            tmp.setPointBT(-1);
            tmp.setPointBTL(-1);
            tmp.setPointGK(-1);
            tmp.setPointCK(-1);
            pointService.save(tmp);
            return true;
        } catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
            throw new NotFoundException(notFoundInDB.getMessage());
        }
    }

    public boolean addTeacherToClass(String teacherId, String classId) throws NotFoundException {
        ClassModel classModel = findById(classId);
        if(classModel.getTeacherId() != null)
            return false;
		TeacherModel teacher = null;
		try {
			teacher = teacherDAO.findById(teacherId);
		} catch (com.hcmutap.elearning.exception.NotFoundInDB notFoundInDB) {
			throw new RuntimeException(notFoundInDB);
		}
		classModel.setTeacherId(teacher.getId());
        classModel.setTeacherName(teacher.getFullName());
        update(classModel);
        return true;
    }

    @Override
    public boolean NhapDiem(String studentId, String classId, PointDTO point) throws NotFoundException {
        List<PointModel> listPoint = pointDAO.findBy("studentId", studentId);
        for (PointModel item : listPoint) {
            if (item.getClassId().equals(classId)) {
                PointModel pointUpdate = new PointModel(item.getFirebaseId(), item.getId(), item.getStudentId(), item.getStudentName(),
                        item.getCourseId(), item.getCourseName(), item.getClassId(), item.getClassName(),item.getSemesterId(),
                        item.isState(),point.getPointBT(), point.getPointBTL(), point.getPointGK(),point.getPointCK());
                pointService.update(pointUpdate);
                return true;
            }
        }
        return false;
    }

    @Override
    public void NhapDiemCaLop(String classId, List<PointDTO> listPoint) throws NotFoundException {
        List<PointModel> listPointClass = pointDAO.findBy("classId", classId);
        for(PointModel item: listPointClass){
            for(PointDTO point: listPoint){
                if(item.getStudentId().equals(point.getStudentId())){
                    NhapDiem(item.getStudentId(), classId, point);
                    break;
                }
            }
        }
    }

    @Override
    public InfoClassModel getClassDocs(String classId) throws NotFoundException {
        ClassModel classModel = findById(classId);
        return infoService.getClassInfo(classModel.getInfoId());
    }

    @Override
    public boolean updateTileOfDoc(String classId, Document docCurrent, String newTitle) throws NotFoundException {
        ClassModel classModel = findById(classId);
        return infoService.updateTile(classModel.getInfoId(), docCurrent, newTitle);
    }

    @Override
    public boolean addFileOfDoc(String classId, Document docCurrent, FileInfo file) throws NotFoundException {
        ClassModel classModel = findById(classId);
        return infoService.addFile(classModel.getInfoId(), docCurrent, file);
    }

    @Override
    public boolean deleteFileOfDoc(String classId, Document docCurrent, FileInfo file) throws NotFoundException {
        ClassModel classModel = findById(classId);
        return infoService.deleteFile(classModel.getInfoId(), docCurrent, file);
    }

    @Override
    public boolean addNewDoc(String classId) throws NotFoundException {
        ClassModel classModel = findById(classId);
        return infoService.addNewDoc(classModel.getInfoId());
    }

    @Override
    public boolean addDoc(String classId, Document doc) throws NotFoundException {
        ClassModel classModel = findById(classId);
        return infoService.addDoc(classModel.getInfoId(), doc);
    }

    @Override
    public boolean deleteDoc(String classId, Document doc) throws NotFoundException {
        ClassModel classModel = findById(classId);
        return infoService.deleteDoc(classModel.getInfoId(), doc);
    }


    public boolean isExist(String id) {
        return classDAO.findBy("classId", id, Options.OptionBuilder.Builder().setEqual().build()) != null;
    }

    @Override
    public Page<ClassModel> getPage(String key, String id, int page, int size) {
        List<ClassModel> listClass = getClassOfCourse(id);
        for(ClassModel classModel : listClass) {
            if(!classModel.getClassId().contains(id))
                listClass.remove(classModel);
        }
        return new PageImpl<>(listClass, PageRequest.of(page - 1, size), listClass.size());
    }
}