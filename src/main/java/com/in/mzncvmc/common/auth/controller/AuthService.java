package com.in.mzncvmc.common.auth.controller;

import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersRepository;
import com.in.mzncvmc.content.users.account.PasswordChangeDto;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final AuthRepository authRepository;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder,
                       UsersRepository usersRepository,
                       AuthRepository authRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.authRepository = authRepository;
    }

    @Transactional
    public void resetPassword(String username, String userFirstPassword) {

        // 0. 사용자 조회
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 1. 비밀번호 초기화 후 저장
        String encoded = passwordEncoder.encode(userFirstPassword);
        int updated = authRepository.resetPassword(username, encoded);

        if (updated == 0) {
            throw new IllegalStateException("비밀번호 초기화 실패");
        }

    }
}

