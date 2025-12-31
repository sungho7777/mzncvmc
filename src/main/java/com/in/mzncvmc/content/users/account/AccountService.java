package com.in.mzncvmc.content.users.account;

import com.in.mzncvmc.content.company.CompanyRepository;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AccountService {
    private PasswordEncoder passwordEncoder;
    private UsersRepository usersRepository;

    @Autowired
    public AccountService(PasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void changePassword(String username, PasswordChangeDto request) {

        // 0. 사용자 조회
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 1. 현재 비밀번호 검증
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 2. 새 비밀번호 확인
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }

        // 3. 새 비밀번호 암호화 후 저장
        //user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 3. 새 비밀번호 암호화 후 저장
        String encoded = passwordEncoder.encode(request.getNewPassword());
        int updated = usersRepository.updatePassword(username, encoded);

        if (updated == 0) {
            throw new IllegalStateException("비밀번호 변경 실패");
        }
    }

}
