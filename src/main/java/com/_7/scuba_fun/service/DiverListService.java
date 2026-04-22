package com._7.scuba_fun.service;

import com._7.scuba_fun.mapper.DiverListMapper;
import com._7.scuba_fun.model.DiverList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 潛水員列表服務層
 */
@Service
public class DiverListService {

    @Autowired
    private DiverListMapper diverListMapper;

    public List<DiverList> getAllDivers() {
        return diverListMapper.findAll();
    }

    public DiverList getDiverById(Integer diverId) {
        return diverListMapper.findById(diverId);
    }

    public DiverList createDiver(DiverList diverList) {
        diverListMapper.insert(diverList);
        return diverList; // 包含自動生成的 ID
    }

    public void updateDiver(DiverList diverList) {
        diverListMapper.update(diverList);
    }

    public void deleteDiver(Integer diverId) {
        diverListMapper.delete(diverId);
    }
}
