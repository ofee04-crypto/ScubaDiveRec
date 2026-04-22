package com._7.scuba_fun.controller;

import com._7.scuba_fun.model.DivingRecord;
import com._7.scuba_fun.service.DivingRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 這是針對 DivingRecordController 的單元測試。
 * 透過 WebMvcTest 與 MockBean，我們成功隔離了真實資料庫與 Service，專注於測試 Controller 的路由邏輯。
 */
@WebMvcTest(DivingRecordController.class)
public class DivingRecordControllerTest {

    // 模擬 HTTP 請求的工具，讓我們不用啟動整台 Tomcat 也能發送 GET/POST 測試
    @Autowired
    private MockMvc mockMvc;

    // 產生一個假的 Service 放進 Spring 容器，防止 API 真的跑去連 MySQL 資料庫
    @MockBean
    private DivingRecordService divingRecordService;

    // 將 Java 物件與 JSON 字串互相轉換的核心工具
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 測試：取得所有潛水紀錄 (GET /api/records)
     */
    @Test
    public void testGetAllRecords() throws Exception {
        // ==========================================
        // 1. Arrange (準備測試資料與設定假造的行為)
        // ==========================================
        DivingRecord record1 = new DivingRecord();
        record1.setRecId(1);
        record1.setDivingType("岸潛");

        // 當 Controller 呼叫 getAllRecords() 時，不查資料庫，強制回傳我們做好的 record1 列表
        when(divingRecordService.getAllRecords()).thenReturn(Arrays.asList(record1));

        // ==========================================
        // 2. Act & Assert (執行請求與驗證結果)
        // ==========================================
        mockMvc.perform(get("/api/records"))
                // 驗證 1：HTTP 狀態碼是否為 200 OK
                .andExpect(status().isOk())
                // 驗證 2：回傳的 JSON 陣列第 0 筆資料的 recId 是否為 1
                .andExpect(jsonPath("$[0].recId").value(1))
                // 驗證 3：divingType 是否為 "岸潛"
                .andExpect(jsonPath("$[0].divingType").value("岸潛"));
    }

    /**
     * 測試：新增潛水紀錄 (POST /api/records)
     */
    @Test
    public void testCreateRecord() throws Exception {
        // ==========================================
        // 1. Arrange (準備階段)
        // ==========================================
        // 建立要送出的請求資料 (無 ID)
        DivingRecord inputRecord = new DivingRecord();
        inputRecord.setDivingType("船潛");

        // 建立假造的儲存後資料 (包含模擬產生出來的 ID)
        DivingRecord savedRecord = new DivingRecord();
        savedRecord.setRecId(2);
        savedRecord.setDivingType("船潛");

        // 當 Service 執行 createRecord 收到任何物件時，一律回傳帶有 ID 的 savedRecord
        when(divingRecordService.createRecord(any(DivingRecord.class))).thenReturn(savedRecord);

        // ==========================================
        // 2. Act & Assert (執行與驗證階段)
        // ==========================================
        mockMvc.perform(post("/api/records")
                // 指定傳送的內容型態是 JSON
                .contentType(MediaType.APPLICATION_JSON)
                // 將 inputRecord 物件轉成實體的 JSON 字串發送
                .content(objectMapper.writeValueAsString(inputRecord)))
                
                // 驗證 1：狀態碼為 201 Created (資源成功建立)
                .andExpect(status().isCreated())
                // 驗證 2：回傳 JSON 的 recId 是否為 2
                .andExpect(jsonPath("$.recId").value(2))
                .andExpect(jsonPath("$.divingType").value("船潛"));
    }
}
