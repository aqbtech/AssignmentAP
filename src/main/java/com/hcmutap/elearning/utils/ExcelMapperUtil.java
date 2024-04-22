package com.hcmutap.elearning.utils;

import com.hcmutap.elearning.dto.ColName;
import com.hcmutap.elearning.exception.ConvertExcelToObjectException;
import com.hcmutap.elearning.exception.FieldNotFound;
import com.hcmutap.elearning.exception.MappingException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class ExcelMapperUtil {
	private static ExcelMapperUtil excelMapper = null;

	public static ExcelMapperUtil getInstance() {
		if (excelMapper == null) {
			excelMapper = new ExcelMapperUtil();
		}
		return excelMapper;
	}
	private Integer getCellIndex(XSSFRow row, String givenValue) {
		for (Cell cell : row) {
			if (cell.getCellType() == CellType.STRING) {
				String cellValue = cell.getStringCellValue();
				if (cellValue.equalsIgnoreCase(givenValue)) {
					return cell.getColumnIndex();
				}
			}
		}
		return null;
	}
	public <T> Map<String, Integer> getHeaders(XSSFRow row, Class<T> tClass) throws FieldNotFound {
		Map<String, Integer> headers = new HashMap<>();
		Field[] fields = tClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(ColName.class)) {
				ColName colName = field.getAnnotation(ColName.class);
				Integer cellIndex = getCellIndex(row, colName.value());
				if (cellIndex != null) {
					headers.put(colName.value(), cellIndex);
				} else if (!colName.optional()) {
					throw new FieldNotFound("Field "+ colName.value() +"not found in excel");
				}
			}
		}
		return headers;
	}

	private String getValueOfCell(XSSFRow row, int cellIndex ) {
		Cell cell = row.getCell(cellIndex);
		String cellValue = null;
		if (cell != null) {
			cellValue = switch (cell.getCellType()) {
				case STRING -> cell.getStringCellValue();
				case NUMERIC -> Integer.toString((int)cell.getNumericCellValue());
				case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
				case FORMULA -> cell.getCellFormula();
				default -> "";
			};
		}
		return cellValue;
	}
	public <T> Optional<List<T>> readAndConvert(MultipartFile file, Class<T> clazz) throws ConvertExcelToObjectException {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet worksheet = workbook.getSheetAt(0);
			Map<String, Integer> headers = getHeaders(worksheet.getRow(0), clazz);
			List<T> result = new ArrayList<>();
			Field[] fields = clazz.getDeclaredFields();
			int classFieldCount = (int) Arrays.stream(fields)
				.filter(field -> field.isAnnotationPresent(ColName.class))
				.filter(field -> !field.getAnnotation(ColName.class).optional())
				.count();
			if (classFieldCount > headers.size()) {
				throw new MappingException("Error when mapping excel because given data is not enough to require data");
			}
			for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
				Map<String, String> FieldPool = new HashMap<>();
				for (Field field : fields) {
					if (field.isAnnotationPresent(ColName.class)) {
						ColName colName = field.getAnnotation(ColName.class);
						if(colName.optional() && !headers.containsKey(colName.value())) {
							FieldPool.put(field.getName(), null);
						} else if (!headers.containsKey(colName.value())) {
							throw new FieldNotFound("Field "+ colName.value() +"not found in excel");
						} else {
							FieldPool.put(field.getName(), getValueOfCell(worksheet.getRow(i), headers.get(colName.value())));
						}
					}
				}
				String json = MapperUtil.getInstance().toJson(FieldPool);
				T model = MapperUtil.getInstance().toModel(json, clazz);
				result.add(model);
			}
			return Optional.of(result);
		} catch (MappingException | IOException | FieldNotFound e) {
			throw new ConvertExcelToObjectException(e.getMessage());
		}
	}
}
