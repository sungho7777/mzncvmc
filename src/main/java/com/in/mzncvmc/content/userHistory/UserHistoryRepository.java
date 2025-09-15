package com.in.mzncvmc.content.userHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    List<UserHistory> findTop100ByUsernameOrderByCreatedAtDesc(String username);

    @Query("SELECT h " +
            " FROM UserHistory h " +
            "WHERE h.actionType IN :actionTypes " +
            "ORDER BY h.createdAt DESC"
    )
    List<UserHistory> findByActionTypes(@Param("actionTypes") List<String> actionTypes);

    List<UserHistory> findByUsernameAndCreatedAtBetweenOrderByCreatedAtDesc(
            String username, LocalDateTime start, LocalDateTime end);
}
