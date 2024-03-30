package com.hcmutap.elearning.controller.api;

import com.hcmutap.elearning.model.PointModel;
import com.hcmutap.elearning.service.IPointService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PointAPI {
    @Resource
    private IPointService pointService;
    @GetMapping("/points/findAll")
    public List<PointModel> findAll() {
        return pointService.findAll();
    }
    @GetMapping("/points/findById")
    public PointModel findById(@RequestParam String id) {
        return pointService.findById(id);
    }
    @PostMapping("/points")
    public void save(@RequestBody PointModel pointModel) {
        pointService.save(pointModel);
    }
    @PutMapping("/points")
    public void update(@RequestBody PointModel pointModel) {
        pointService.update(pointModel);
    }
    @DeleteMapping("/points")
    public void delete(@RequestBody List<String> ids) {
        pointService.delete(ids);
    }
}
