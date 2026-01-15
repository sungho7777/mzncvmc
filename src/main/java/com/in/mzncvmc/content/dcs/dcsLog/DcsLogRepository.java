package com.in.mzncvmc.content.dcs.dcsLog;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface DcsLogRepository extends JpaRepository<DcsLog, Long> {

    Optional<DcsLog> findByLogId(Long logId);

    @Modifying
    @Transactional
    @Query("""
        DELETE 
          FROM DcsLog d
         WHERE d.serverNo = :serverNo
           AND d.logDate = :logDate
    """)
    void deleteByServerNoAndLogDate(
            @Param("serverNo") String serverNo,
            @Param("logDate") String logDate
    );

}
