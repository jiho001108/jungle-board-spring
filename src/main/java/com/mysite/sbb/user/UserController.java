package com.mysite.sbb.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        /* 입력받은 2개의 비밀번호가 일치하는지 확인 */
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다."); /* bindingResult.rejectValue(필드명, 오류 코드, 오류 메시지) */
            return "signup_form";
        }

        /* try-catch 중복 회원 가입 방지 */
        try {
            userService.create(userCreateForm.getUsername(), 
                    userCreateForm.getEmail(), userCreateForm.getPassword1());
            /* 데이터 무결성 위반(DataIntegrityViolationException)
            * 사용자 ID or 이메일 주소가 이미 존재할 경우 */
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            /* 글로벌 오류를 추가 */
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e) { /* 데이터 무결성 위반을 제외한 일반적인 오류를 발생시킬 때 사용 */
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage()); /* e.getMessage(): 해댱 예외에 대한 구체적인 오류 메세지 출력 */
            return "signup_form";
        }

        return "redirect:/";
    }

    /* 로그인 페이지 매핑
    * 실제 로그인을 진행하는 @PostMapping 방식의 메서드는,
    * 스프링 시큐리티가 대신 처리하므로 우리가 직접 코드를 작성하여 구현할 필요가 없다. */
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}