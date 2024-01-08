package com.mysite.sbb;

import com.mysite.sbb.user.SiteUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.question.QuestionService;

import java.util.UUID;

@SpringBootTest
class SbbApplicationTests {

    @Autowired
    private QuestionService questionService;

    @Test
    void testJpa() {
        for (int i = 1; i <= 300; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            // 유니크한 사용자 이름 생성
            String username = UUID.randomUUID().toString();

            // 새로운 사용자 객체 생성
            SiteUser user = new SiteUser();
            user.setUsername(username);
            user.setPassword("somePassword");

            // UUID를 사용하여 유니크한 이메일 생성
            String email = username + "@example.com";
            user.setEmail(email);
            this.questionService.create(subject, content, user);
        }
    }
}
