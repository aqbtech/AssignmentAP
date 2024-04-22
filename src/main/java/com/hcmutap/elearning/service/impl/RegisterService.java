package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.constant.SystemConstant;
import com.hcmutap.elearning.dto.RegisterDTO;
import com.hcmutap.elearning.dto.StudentResDTO;
import com.hcmutap.elearning.dto.TeacherResDTO;
import com.hcmutap.elearning.exception.*;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.model.StudentModel;
import com.hcmutap.elearning.model.TeacherModel;
import com.hcmutap.elearning.model.UserModel;
import com.hcmutap.elearning.service.IPointService;
import com.hcmutap.elearning.service.IRegisterService;
import com.hcmutap.elearning.utils.MapperUtil;
import com.hcmutap.elearning.utils.ModelBuilderUtil;

import com.hcmutap.elearning.validator.RegisterDTOValidator;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
	public String register(ModelMap model) throws MappingException {
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
	private RegisterDTOValidator registerDTOValidator;
	@Autowired
	public void setRegisterDTOValidator(RegisterDTOValidator registerDTOValidator) {
		this.registerDTOValidator = registerDTOValidator;
	}
	@Autowired
	private MessageSource messageSource;
	@Override
	public Map<String, String> register(MultipartFile file) throws ConvertExcelToObjectException, MappingException {
		Map<String,String> result = new HashMap<>();
		Optional<List<RegisterDTO>> list;
		ExcelService<RegisterDTO> excelService = new ExcelService<>();
		list = excelService.readAndConvert(file, RegisterDTO.class);
		int complete = 0;
		if (list.isPresent()) {
			for (RegisterDTO registerDTO : list.get()) {
				Errors errors = new BeanPropertyBindingResult(registerDTO, "registerForm");
				registerDTOValidator.validate(registerDTO, errors);
				if (errors.hasErrors()) {
					for (ObjectError error : errors.getAllErrors()) {
						for (String errorCode : Objects.requireNonNull(error.getCodes())) {
							try {
								String errorMessage = messageSource.getMessage(errorCode, error.getArguments(), Locale.getDefault());
								result.put(registerDTO.getUsername(), "Failed to add account, message: " + errorMessage);
							} catch (NoSuchMessageException e) {
								// This error code does not exist in your properties file, continue with the next one
								continue;
							}
						}
					}
					// fixme : use this code instead of above code, above code to fix rare fault
//					result.put(registerDTO.getUsername(),
//							"Failed to add account, message: " + errors.getAllErrors());
				} else {
					ModelMap modelMap = MapperUtil.getInstance().toModelMapFromDTO(registerDTO);
					String message = register(modelMap);
					if (!message.equals("Success")) {
						if (!result.containsKey(registerDTO.getUsername())) {
							result.put(registerDTO.getUsername(), "Failed to add account, message: " + message);
						}
					} else {
						++complete;
					}
				}
			}
			result.put("Complete", Integer.toString(complete));
			result.put("Error", Integer.toString(result.size() - complete));
		} else {
			result.put("Error", "No data in file");
		}
		return result;
	}
	private IPointService pointService;
	@Autowired
	public void setPointService(IPointService pointService) {
		this.pointService = pointService;
	}
	@Override
	public String deleteAccount(String id, String type) {
		StringBuilder result = new StringBuilder();
		try {
			if (SystemConstant.ROLE_TEACHER.equalsIgnoreCase(type)) {
				TeacherModel teacher = teacherService.findById(id);
				if (teacher.getClasses().isEmpty()) {
					List<String> del = new ArrayList<>();
					del.add(teacher.getFirebaseId());
					teacherService.delete(del);
					userService.delete(teacher.getUsername());
					result.append("Xóa giáo viên ").append(teacher.getId()).append("thành công!");
				} else {
					result.append("Giáo viên ").append(teacher.getId()).append(" đang dạy lớp");
					for (String classId : teacher.getClasses()) {
						result.append(" ").append(classId);
					}
					result.append(" nên chưa thể xóa!");
				}
			} else if (SystemConstant.ROLE_STUDENT.equalsIgnoreCase(type)){
				StudentModel student = studentService.findById(id);
				List<String> del = new ArrayList<>();
				del.add(student.getFirebaseId());
				studentService.delete(del);
				userService.delete(student.getUsername());
				List<PointModel> points = pointService.getListPointByStudentId(student.getId());
				if (!points.isEmpty()) {
					List<String> delPoint = new ArrayList<>();
					for (PointModel point : points) {
						delPoint.add(point.getFirebaseId());
					}
					pointService.delete(delPoint);
				}
			} else {
				result.append("Loại tài khoản không hợp lệ!");
			}
		} catch (NotFoundException e) {
			throw new CustomRuntimeException(e.getMessage(), "202");
		} catch (TransactionalException e) {
			throw new CustomRuntimeException(e.getMessage(),"100");
		}
		return result.toString();
	}

	@Override
	public String updateAccount(RegisterDTO form, String type) {
		try {
			if (SystemConstant.ROLE_TEACHER.equalsIgnoreCase(type)) {
				TeacherModel teacher = teacherService.findById(form.getId());
				MapperUtil.getInstance().updateModelFromDto(teacher, form);
				teacherService.update(teacher);
				return "Cập nhật thông tin giáo viên thành công!";
			} else if (SystemConstant.ROLE_STUDENT.equalsIgnoreCase(type)) {
				StudentModel student = studentService.findById(form.getId());
				MapperUtil.getInstance().updateModelFromDto(student, form);
				studentService.update(student);
				return "Cập nhật thông tin học sinh thành công!";
			}
		} catch (NotFoundException e) {
			throw new CustomRuntimeException(e.getMessage(), "404");
		} catch (MappingException e) {
			throw new CustomRuntimeException(e.getMessage(),"101");
		}
		return "";
	}

}
