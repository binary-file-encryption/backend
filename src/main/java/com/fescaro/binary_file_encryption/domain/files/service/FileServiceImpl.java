package com.fescaro.binary_file_encryption.domain.files.service;

import com.fescaro.binary_file_encryption.domain.files.dto.FileResponseDto;
import com.fescaro.binary_file_encryption.domain.files.repository.EncryptedFileInfoRepository;
import com.fescaro.binary_file_encryption.domain.files.repository.OriginalFileInfoRepository;
import com.fescaro.binary_file_encryption.global.encryption.aes.util.AESEncryptionUtil;
import com.fescaro.binary_file_encryption.global.storage.service.FileStorageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FilesService {
    private final OriginalFileInfoRepository originalFileInfoRepository;
    private final EncryptedFileInfoRepository encryptedFileInfoRepository;
    private final FileStorageService fileStorageService;
    private final AESEncryptionUtil aesEncryptionUtil;
}
