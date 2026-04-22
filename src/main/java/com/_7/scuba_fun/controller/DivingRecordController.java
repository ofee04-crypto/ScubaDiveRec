package com._7.scuba_fun.controller;

import com._7.scuba_fun.model.DivingRecord;
import com._7.scuba_fun.service.DivingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class DivingRecordController {

    @Autowired
    private DivingRecordService divingRecordService;

    // 取得所有潛水紀錄
    @GetMapping
    public ResponseEntity<List<DivingRecord>> getAllRecords() {
        return ResponseEntity.ok(divingRecordService.getAllRecords());
    }

    // 根據 ID 取得特定潛水紀錄
    @GetMapping("/{id}")
    public ResponseEntity<DivingRecord> getRecordById(@PathVariable("id") Integer id) {
        DivingRecord record = divingRecordService.getRecordById(id);
        if (record != null) {
            return ResponseEntity.ok(record);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 新增潛水紀錄
    @PostMapping
    public ResponseEntity<DivingRecord> createRecord(@RequestBody DivingRecord divingRecord) {
        DivingRecord createdRecord = divingRecordService.createRecord(divingRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord);
    }

    // 更新潛水紀錄
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecord(@PathVariable("id") Integer id, @RequestBody DivingRecord divingRecord) {
        DivingRecord existingRecord = divingRecordService.getRecordById(id);
        if (existingRecord == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        divingRecord.setRecId(id);
        divingRecordService.updateRecord(divingRecord);
        return ResponseEntity.ok().build();
    }

    // 刪除潛水紀錄
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable("id") Integer id) {
        DivingRecord existingRecord = divingRecordService.getRecordById(id);
        if (existingRecord == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        divingRecordService.deleteRecord(id);
        return ResponseEntity.ok().build();
    }
}
