package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
/* 스프링 시큐리티의 UserDetailsService는 loadUserByUsername 메서드를 구현하도록 강제하는 인터페이스
 loadUserByUsername 메서드는 사용자명(username)으로 스프링 시큐리티의 사용자(User) 객체를 조회하여 리턴하는 메서드 */
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    /* 사용자명으로 SiteUser 객체를 조회하고,
    만약 사용자명에 해당하는 데이터가 없을 경우에는 UsernameNotFoundException을 발생시킨다.
    그리고 사용자명이 ‘admin’인 경우에는 ADMIN 권한(ROLE_ADMIN)을 부여하고
    그 이외의 경우에는 USER 권한(ROLE_USER)을 부여했다.
    마지막으로 User 객체를 생성해 반환하는데,
    이 객체는 스프링 시큐리티에서 사용하며 User 생성자에는 사용자명, 비밀번호, 권한 리스트가 전달됨 */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> _siteUser = this.userRepository.findByusername(username);
        if (_siteUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        SiteUser siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>(); /* 왜 리스트? -> 다중 권한!, 표준화된 방식 */
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        /* 리턴한 user 객체의 비밀번호가 사용자로부터 입력받은 비밀 번호와 일치하는지를 검사하는 기능을 갖추고 있다. */
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}