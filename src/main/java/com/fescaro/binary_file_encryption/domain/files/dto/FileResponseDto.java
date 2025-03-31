package com.fescaro.binary_file_encryption.domain.files.dto;

import com.fescaro.binary_file_encryption.domain.files.entity.EncryptedFileInfo;
import com.fescaro.binary_file_encryption.domain.files.entity.OriginalFileInfo;
import java.time.LocalDateTime;

public record FileResponseDto(
        String originalFileName,
        String savedOriginalFileName,
        String encryptedFileName,
        String savedEncryptedFileName,
        String ivValue,
        LocalDateTime updatedAt
) {
    public static FileResponseDto of(OriginalFileInfo originalFileInfo, EncryptedFileInfo encryptedFileInfo) {
        return new FileResponseDto(
                originalFileInfo.getFileName(),
                originalFileInfo.getSavedFileName(),
                encryptedFileInfo.getFileName(),
                encryptedFileInfo.getSavedFileName(),
                encryptedFileInfo.getIvValue(),
                encryptedFileInfo.getUpdatedAt());
    }
}
