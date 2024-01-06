package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

/* 질문 엔티티
* id
* subject
* content
* createDate */

@Getter
@Setter // getSubject()등의 메서드를 자동으로 생성
@Entity
public class Question {
    @Id // id 속성을 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터를 저장할 때 해당 속성에 값을 일일이 입력하지 않아도 자동으로 1씩 증가하여 저장
    private Integer id;

    @Column(length = 200) // 열의 길이를 설정
    private String subject;

    @Column(columnDefinition = "TEXT") // 텍스트를 열 데이터로 넣을 수 있음을 의미, 글자 수 제한 불가
    private String content;

    private LocalDateTime createDate; // DB에서 create_date라는 열 이름으로 설정

    /* 질문에서 답변을 참조: 1:N 관계
    * cascade = CascadeType.REMOVE: 질문 삭제하면 그에 달린 답변 모두 삭제 */
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList; 
    /* answerList: Answer 객체들로 구성
    * 질문에서 답변을 참조하려면 question.getAnswerList()를 호출하면 됨 */
    
    @ManyToOne
    private SiteUser author;
    
    private LocalDateTime modifyDate;
    
    @ManyToMany
    Set<SiteUser> voter;
}