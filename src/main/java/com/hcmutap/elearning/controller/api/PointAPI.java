package com.hcmutap.elearning.controller.api;

import com.hcmutap.elearning.exception.NotFoundException;
import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.IPointService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PointAPI {
	private static final Logger logger = LoggerFactory.getLogger(PointAPI.class);
	@Resource
	private IPointService pointService;
	@GetMapping("/points/findAll")
	public List<PointModel> findAll() {
		try{
			return pointService.findAll();
		}catch (Exception e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
			return null;
		}
	}
	@GetMapping("/points/findById")
	public PointModel findById(@RequestParam String id) {
		try {
			return pointService.findById(id);
		} catch (NotFoundException e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
			return null;
		}
	}
	@PostMapping("/points")
	public void save(@RequestBody PointModel pointModel) {
		try{
			pointService.save(pointModel);
		}catch (Exception e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
		}
	}
	@PutMapping("/points")
	public void update(@RequestBody PointModel pointModel) {
		try {
			pointService.update(pointModel);
		} catch (NotFoundException e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
		}
	}
	@DeleteMapping("/points")
	public void delete(@RequestBody List<String> ids) {
		try {
			pointService.delete(ids);
		} catch (NotFoundException e) {
//			throw new RuntimeException(e);
			logger.error(String.valueOf(new RuntimeException(e)));
		}
	}
}
