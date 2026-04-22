package com._7.scuba_fun.controller;

import com._7.scuba_fun.model.DiverList;
import com._7.scuba_fun.service.DiverListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/divers")
public class DiverListController {

    @Autowired
    private DiverListService diverListService;

    // 取得所有潛水員
    @GetMapping
    public ResponseEntity<List<DiverList>> getAllDivers() {
        return ResponseEntity.ok(diverListService.getAllDivers());
    }

    // 根據 ID 取得特定潛水員
    @GetMapping("/{id}")
    public ResponseEntity<DiverList> getDiverById(@PathVariable("id") Integer id) {
        DiverList diver = diverListService.getDiverById(id);
        if (diver != null) {
            return ResponseEntity.ok(diver);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 新增潛水員
    @PostMapping
    public ResponseEntity<DiverList> createDiver(@RequestBody DiverList diverList) {
        DiverList createdDiver = diverListService.createDiver(diverList);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDiver);
    }

    // 更新潛水員資訊
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDiver(@PathVariable("id") Integer id, @RequestBody DiverList diverList) {
        DiverList existingDiver = diverListService.getDiverById(id);
        if (existingDiver == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        diverList.setDiverId(id);
        diverListService.updateDiver(diverList);
        return ResponseEntity.ok().build();
    }

    // 刪除潛水員
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiver(@PathVariable("id") Integer id) {
        DiverList existingDiver = diverListService.getDiverById(id);
        if (existingDiver == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        diverListService.deleteDiver(id);
        return ResponseEntity.ok().build();
    }
}
