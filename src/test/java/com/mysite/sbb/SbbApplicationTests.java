package com.mysite.sbb;

import java.time.LocalDateTime;
import java.util.List;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest /* 스프링 부트의 테스트 클래스임을 의미 */
class SbbApplicationTests {

    @Autowired /* 필드 주입을 통한 의존성 주입: QuestionRepository의 객체 주입 */
    private QuestionRepository questionRepository;

//    private QuestionRepository questionRepository;
//
//    public SbbApplicationTests(QuestionRepository questionRepository) {
//        this.questionRepository = questionRepository;
//    }

//    @BeforeEach
//    public void beforeEach() {
//        questionRepository.deleteAll();
//    }

    @AfterEach
    public void afterEach() {
        questionRepository.deleteAll();
    }

    @Test
    public void questionSaveTest() {
        Question q1 = new Question(); /* q1 이라는 질문 엔티티 객체 생성 */
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문을 DB에 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문 저장
    }

    @Test
    public void findAllTest() {
        questionSaveTest();

        List<Question> all = this.questionRepository.findAll();
        Assertions.assertEquals(2, all.size());

        Question q = all.get(0);
        System.out.println(q.getSubject());
        Assertions.assertEquals("sbb가 무엇인가요?", q.getSubject());
    }
}
