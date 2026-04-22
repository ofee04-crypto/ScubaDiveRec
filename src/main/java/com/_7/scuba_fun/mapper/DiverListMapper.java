package com._7.scuba_fun.mapper;

import com._7.scuba_fun.model.DiverList;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DiverListMapper {

    List<DiverList> findAll();

    DiverList findById(Integer diverId);

    void insert(DiverList diverList);

    void update(DiverList diverList);

    void delete(Integer diverId);
}
