package com.fescaro.binary_file_encryption.domain.files.repository;

import com.fescaro.binary_file_encryption.domain.files.entity.EncryptedFileInfo;
import com.fescaro.binary_file_encryption.domain.files.entity.OriginalFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncryptedFileInfoRepository extends JpaRepository<EncryptedFileInfo, Long> {
}
