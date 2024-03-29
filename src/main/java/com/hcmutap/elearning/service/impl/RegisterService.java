package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dto.StudentResDTO;
import com.hcmutap.elearning.dto.TeacherResDTO;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IRegisterService;
import com.hcmutap.elearning.utils.MapperUtil;
import com.hcmutap.elearning.utils.ModelBuilderUtil;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.Objects;

@Service
public class RegisterService implements IRegisterService {
	@Resource
	private TeacherService teacherService;
	@Resource
	private StudentService studentService;
	@Resource
	private UserService userService;
	private void registerTeacher(TeacherResDTO teacher) {
		if(userService.isExist(teacher.getUsername()) && !teacherService.isExist(teacher.getId())) {
			System.out.println("Username is already exist"); // throw exist exception
			return;
		} else  {
			UserModel userModel = ModelBuilderUtil.buildModelFromDTO(new UserModel(), teacher);
			userModel.setUsrId(teacher.getId());
			TeacherModel teacherModel = ModelBuilderUtil.buildModelFromDTO(new TeacherModel(), teacher);
			userService.save(userModel);
			teacherService.save(teacherModel);
		}

	}
	private void registerStudent(StudentResDTO student) {
		if(userService.isExist(student.getUsername()) && !studentService.isExist(student.getId())) {
			System.out.println("Username is already exist"); // throw exist exception
			return;
		} else  {
			UserModel userModel = ModelBuilderUtil.buildModelFromDTO(new UserModel(), student);
			userModel.setUsrId(student.getId());
			StudentModel studentModel = ModelBuilderUtil.buildModelFromDTO(new StudentModel(), student);
			userService.save(userModel);
			studentService.save(studentModel);
		}
	}
	@Override
	public void register(ModelMap model) {
		if(Objects.equals((Objects.requireNonNull(model.getAttribute("role"))).toString().toLowerCase(), "teacher")) {
			TeacherResDTO teacher = MapperUtil.getInstance().toModelFromModelMap(model, TeacherResDTO.class);
			registerTeacher(teacher);
		}
		else if(Objects.equals(Objects.requireNonNull(model.getAttribute("role")).toString().toLowerCase(), "student")) {
			StudentResDTO student = MapperUtil.getInstance().toModelFromModelMap(model, StudentResDTO.class);
			registerStudent(student);
		}
	}

}
