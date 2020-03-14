package com.cjh.ttt.toutiao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TouTiaoApiServiceTest {

    @Autowired
    private TouTiaoApiService touTiaoApiService;

    @Test
    void code2Session() {
        touTiaoApiService.code2Session("");
    }

    @Test
    void getAccessToken() {
        touTiaoApiService.getAccessToken();
    }

    @Test
    void sendTemplate() {
        touTiaoApiService.sendTemplate("", "", "", "");
    }

    @Test
    void createQrCode() {
        System.out.println(touTiaoApiService.createQrCode());
    }
}