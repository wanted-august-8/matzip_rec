package com.matzip.api.matzip_api.global.auth.service;

import com.matzip.api.matzip_api.domain.user.entity.User;
import com.matzip.api.matzip_api.domain.user.repository.UserRepository;
import com.matzip.api.matzip_api.global.auth.domain.CustomUserDetails;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인 요청 유저 조회
        User loginUser = userRepository.findByAccount(username).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // UserDetails에 담아서 return하면 AutneticationManager가 검증
        return new CustomUserDetails(loginUser);
    }
}
