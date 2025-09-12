package com.in.mzncvmc.common.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    /**
     * 특정 참조 타입과 ID에 연결된 활성 파일 목록 조회
     */
    @Query("SELECT f " +
            " FROM FileEntity f " +
            "WHERE f.referenceType = :referenceType " +
            "  AND f.referenceId = :referenceId " +
            "  AND f.isDeleted = false " +
            "ORDER BY f.uploadDate ASC"
    )
    List<FileEntity> findActiveFilesByReference(@Param("referenceType") String referenceType, @Param("referenceId") Long referenceId);

    /**
     * 파일 ID로 활성 파일 조회
     */
    @Query("SELECT f " +
            " FROM FileEntity f " +
            "WHERE f.fileId = :fileId " +
            "  AND f.isDeleted = false")
    FileEntity findActiveFileById(@Param("fileId") Long fileId);

    /**
     * 특정 사용자가 업로드한 파일 목록 조회
     */
    @Query("SELECT f " +
            " FROM FileEntity f " +
            "WHERE f.createdBy = :userId " +
            "  AND f.isDeleted = false " +
            "ORDER BY f.uploadDate DESC")
    List<FileEntity> findFilesByUser(@Param("userId") Long userId);

    /**
     * 저장된 파일명으로 파일 조회
     */
    FileEntity findByStoredFilenameAndIsDeletedFalse(String storedFilename);
}
