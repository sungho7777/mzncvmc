package com.in.mzncvmc.content.users.password;

import com.in.mzncvmc.content.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    // 가장 최근 비밀번호 1건 조회
    Optional<PasswordHistory> findTopByUserIdOrderByCreatedAtDesc(Long userId);

    // N개 제한 시 사용 가능
    List<PasswordHistory> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);
}


