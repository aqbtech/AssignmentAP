package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dto.StudentResDTO;
import com.hcmutap.elearning.dto.TeacherResDTO;
import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IRegisterService;
import com.hcmutap.elearning.utils.MapperUtil;
import com.hcmutap.elearning.utils.ModelBuilderUtil;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RegisterService implements IRegisterService {
	@Resource
	private TeacherService teacherService;
	@Resource
	private StudentService studentService;
	@Resource
	private UserService userService;
	private String registerTeacher(TeacherResDTO teacher) {
		if(userService.isExist(teacher.getUsername())) {
			return "Username is already exist";
		} else if (teacherService.isExist(teacher.getId())) {
			return "Teacher is already exist";
		} else {
			UserModel userModel = ModelBuilderUtil.buildModelFromDTO(new UserModel(), teacher);
			userModel.setUsrId(teacher.getId());
			TeacherModel teacherModel = ModelBuilderUtil.buildModelFromDTO(new TeacherModel(), teacher);
			teacherModel.setClasses(new ArrayList<>());
			teacherModel.setCourses(new ArrayList<>());
			userService.save(userModel);
			teacherService.save(teacherModel);
			return "Success";
		}
	}
	private String registerStudent(StudentResDTO student) {
		if(userService.isExist(student.getUsername())) {
			return "Username is already exist";
		} else if (studentService.isExist(student.getId())) {
			return "Student is already exist";
		} else {
			UserModel userModel = ModelBuilderUtil.buildModelFromDTO(new UserModel(), student);
			userModel.setUsrId(student.getId());
			StudentModel studentModel = ModelBuilderUtil.buildModelFromDTO(new StudentModel(), student);
			studentModel.setClasses(new ArrayList<>());
			studentModel.setCourses(new ArrayList<>());
			studentModel.setFinished_courses(new ArrayList<>());
			userService.save(userModel);
			studentService.save(studentModel);
			return "Success";
		}
	}
	@Override
	public String register(ModelMap model) throws NotFoundException {
		if(Objects.equals((Objects.requireNonNull(model.getAttribute("role"))).toString().toLowerCase(), "teacher")) {
			TeacherResDTO teacher = MapperUtil.getInstance().toModelFromModelMap(model, TeacherResDTO.class);
			return registerTeacher(teacher);
		}
		else if(Objects.equals(Objects.requireNonNull(model.getAttribute("role")).toString().toLowerCase(), "student")) {
			StudentResDTO student = MapperUtil.getInstance().toModelFromModelMap(model, StudentResDTO.class);
			return registerStudent(student);
		}
		return "Role is not valid";
	}

	@Override
	public String register(List<ModelMap> models) throws NotFoundException {
		StringBuilder result = new StringBuilder();
		for(ModelMap model : models) {
			String subResult = register(model);
			if (!subResult.equals("Success")) {
				result.append(subResult).append(" when register user with ID ")
						.append(model.getAttribute("id")).append("\n");
			} else {
				result.append("Success").append("\n");
			}
		}
		return result.toString();
	}

}
