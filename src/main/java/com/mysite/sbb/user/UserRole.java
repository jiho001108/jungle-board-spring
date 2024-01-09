package com.mysite.sbb.user;

import lombok.Getter;


@Getter /* Admin, User 상수는 값을 변경할 필요가 없으므로 setter가 필요 없다. */
public enum UserRole { /* 열거 자료형으로 선언 */
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}