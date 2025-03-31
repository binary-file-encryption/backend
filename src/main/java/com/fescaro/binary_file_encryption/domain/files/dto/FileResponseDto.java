package com.fescaro.binary_file_encryption.domain.files.dto;

import com.fescaro.binary_file_encryption.domain.files.entity.EncryptedFileInfo;
import com.fescaro.binary_file_encryption.domain.files.entity.OriginalFileInfo;
import java.time.LocalDateTime;

public record FileResponseDto(
        Long originalFileId,
        String originalFileName,
        Long encryptedFileId,
        String encryptedFileName,
        String ivValue,
        LocalDateTime updatedAt
) {
    public static FileResponseDto of(OriginalFileInfo originalFileInfo, EncryptedFileInfo encryptedFileInfo) {
        return new FileResponseDto(
                originalFileInfo.getId(),
                originalFileInfo.getFileName(),
                encryptedFileInfo.getId(),
                encryptedFileInfo.getFileName(),
                encryptedFileInfo.getIvValue(),
                encryptedFileInfo.getUpdatedAt());
    }
}
