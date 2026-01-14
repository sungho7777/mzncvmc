package com.in.mzncvmc.content.dcs.dcsLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DcsLogRepository extends JpaRepository<DcsLog, Long> {

    Optional<DcsLog> findByLogId(Long logId);
}
