package com._7.scuba_fun.service;

import com._7.scuba_fun.mapper.DivingRecordMapper;
import com._7.scuba_fun.model.DivingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 潛水紀錄服務層
 */
@Service
public class DivingRecordService {

    @Autowired
    private DivingRecordMapper divingRecordMapper;

    public List<DivingRecord> getAllRecords() {
        return divingRecordMapper.findAll();
    }

    public DivingRecord getRecordById(Integer recId) {
        return divingRecordMapper.findById(recId);
    }

    public DivingRecord createRecord(DivingRecord divingRecord) {
        divingRecordMapper.insert(divingRecord);
        return divingRecord;
    }

    public void updateRecord(DivingRecord divingRecord) {
        divingRecordMapper.update(divingRecord);
    }

    public void deleteRecord(Integer recId) {
        divingRecordMapper.delete(recId);
    }
}
