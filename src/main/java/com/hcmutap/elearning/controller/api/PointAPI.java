package com.hcmutap.elearning.controller.api;

import com.hcmutap.elearning.dao.impl.PointDAO;
import com.hcmutap.elearning.model.PointModel;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PointAPI {
    @Resource
//    private IPointService pointService;
    PointDAO pointDAO;
    @GetMapping("/points/findAll")
    public List<PointModel> findAll() { return pointDAO.findAll(); }
    @GetMapping("/points/findById")
    public PointModel findById(@RequestParam String id) {
        return pointDAO.findById(id);
    }
    @PostMapping("/points")
    public void save(@RequestBody PointModel pointModel) {
        pointDAO.save(pointModel);
    }
    @PutMapping("/points")
    public void update(@RequestBody PointModel pointModel) { pointDAO.update(pointModel); }
    @DeleteMapping("/points")
    public void delete(@RequestBody List<String> ids) {
        ids.forEach(id -> pointDAO.delete(id));
    }
}
