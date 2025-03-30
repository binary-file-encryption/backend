package com.fescaro.binary_file_encryption.domain.files.dto;

import java.time.LocalDateTime;

public record FileResponseDto(
        Long originalFileId,
        String originalFileName,
        Long encryptedFileId,
        String encryptedFileName,
        String ivValue,
        LocalDateTime updatedAt
) {
}
