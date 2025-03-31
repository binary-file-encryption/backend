package com.fescaro.binary_file_encryption.domain.files.repository;

import com.fescaro.binary_file_encryption.domain.files.entity.OriginalFileInfo;
import com.fescaro.binary_file_encryption.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OriginalFileInfoRepository extends JpaRepository<OriginalFileInfo, Long> {
    /**
     * 사용자가 업로드한 파일 목록 조회
     * @param user 요청한 사용자
     * @param pageable updatedAt을 기준으로 정렬
     * @return
     */
    @Query("SELECT of FROM OriginalFileInfo of WHERE of.user = :user")
    Page<OriginalFileInfo> getMyFiles(@Param("user") User user, Pageable pageable);
}
