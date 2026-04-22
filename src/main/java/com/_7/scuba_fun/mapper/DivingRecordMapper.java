package com._7.scuba_fun.mapper;

import com._7.scuba_fun.model.DivingRecord;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DivingRecordMapper {

    List<DivingRecord> findAll();

    DivingRecord findById(Integer recId);

    void insert(DivingRecord divingRecord);

    void update(DivingRecord divingRecord);

    void delete(Integer recId);
}
