package com.matzip.api.matzip_api.global.auth.service;

import com.matzip.api.matzip_api.domain.user.entity.User;
import com.matzip.api.matzip_api.domain.user.repository.UserRepository;
import com.matzip.api.matzip_api.global.auth.dto.SignUpRequestDto;
import com.matzip.api.matzip_api.global.error.ErrorCode;
import com.matzip.api.matzip_api.global.exception.CustomException;
import com.matzip.api.matzip_api.global.util.PasswordManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordManager passwordManager;

    public long signUp(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByAccount(signUpRequestDto.getAccount())) {
            throw new CustomException(ErrorCode.ACCOUNT_ALREADY_REGISTERED);
        }

        return userRepository.save(
            User.builder()
                .account(signUpRequestDto.getAccount())
                .password(passwordManager.encodePassword(signUpRequestDto.getPassword()))
                .build()
        ).getId();
    }
}
