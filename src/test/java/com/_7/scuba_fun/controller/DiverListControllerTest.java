package com._7.scuba_fun.controller;

import com._7.scuba_fun.model.DiverList;
import com._7.scuba_fun.service.DiverListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 測試註解。不啟動整個 Spring Boot，只載入 Controller 層與 Web 相關的元件，提升測試速度。
@WebMvcTest(DiverListController.class)
public class DiverListControllerTest {

    // 模擬發送 HTTP 請求(GET, POST 等)給 Controller。
    @Autowired
    private MockMvc mockMvc;
    // 創造假的 DiverListService，取代原本連真實資料庫的 Service。呼叫 Service時，不查資料庫而是回傳自訂的假資料。
    @MockitoBean
    private DiverListService diverListService;
    // 準備 ObjectMapper 實例，測試中將 Java 物件轉為 JSON 字串
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 測試：取得所有潛水員 (GET /api/divers)
     */
    @Test
    public void testGetAllDivers() throws Exception {
        // ==========================================
        // 1. Arrange (準備階段：準備測試資料與假造行為)
        // ==========================================
        DiverList diver1 = new DiverList();
        diver1.setDiverId(1);
        diver1.setAccount("test_diver");

        // 當 Controller 內部呼叫 diverListService.getAllDivers() 時，
        // 攔截這個呼叫，並強制它回傳我們剛建好的 diver1 列表，不要去查資料庫。
        when(diverListService.getAllDivers()).thenReturn(Arrays.asList(diver1));

        // ==========================================
        // 2. Act & Assert (執行與驗證階段)
        // ==========================================
        // 模擬發送一個 GET 請求到 "/api/divers"
        mockMvc.perform(get("/api/divers"))
                // 驗證 1：預期 HTTP 回傳狀態碼必須是 200 (OK)
                .andExpect(status().isOk())
                // 驗證 2：預期回傳的 JSON 陣列中，第 0 筆資料的 diverId 必須是 1
                // 語法 "$[0]" 代表 JSON 陣列的第一個元素
                .andExpect(jsonPath("$[0].diverId").value(1))
                // 驗證 3：預期回傳的 JSON 陣列中，第 0 筆資料的 account 必須是 "test_diver"
                .andExpect(jsonPath("$[0].account").value("test_diver"));
    }

    /**
     * 測試：新增潛水員 (POST /api/divers)
     */
    @Test
    public void testCreateDiver() throws Exception {
        // 1. Arrange (準備)
        // 準備要傳給 API 的 JSON 請求內容 (此時還沒有 ID)
        DiverList inputDiver = new DiverList();
        inputDiver.setAccount("new_diver");

        // 準備假造的 Service 回傳結果 (模擬資料庫儲存後，產生了 ID 為 2 的結果)
        DiverList savedDiver = new DiverList();
        savedDiver.setDiverId(2);
        savedDiver.setAccount("new_diver");

        // 當 Service 準備儲存「任何 (any)」 DiverList 物件時，強制回傳帶有 ID 的 savedDiver
        when(diverListService.createDiver(any(DiverList.class))).thenReturn(savedDiver);

        // 2. Act & Assert (執行與驗證)
        // 模擬 POST 請求到 "/api/divers"
        mockMvc.perform(post("/api/divers")
                // 宣告傳過去的內容是 JSON 格式
                .contentType(MediaType.APPLICATION_JSON)
                // 用 ObjectMapper 將 inputDiver 物件轉成實體的 JSON 字串塞進 Request Body
                .content(objectMapper.writeValueAsString(inputDiver)))

                // 驗證 1：預期 HTTP 回傳狀態碼必須是 201 (Created，代表新增成功)
                .andExpect(status().isCreated())
                // 驗證 2：預期回傳的 JSON 根節點 "$" 中的 diverId 必須是 2
                .andExpect(jsonPath("$.diverId").value(2))
                // 驗證 3：預期回傳的 account 必須是 "new_diver"
                .andExpect(jsonPath("$.account").value("new_diver"));
    }
}
