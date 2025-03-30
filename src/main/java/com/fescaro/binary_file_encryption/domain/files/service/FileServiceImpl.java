package com.fescaro.binary_file_encryption.domain.files.service;

import com.fescaro.binary_file_encryption.domain.files.repository.EncryptedFileInfoRepository;
import com.fescaro.binary_file_encryption.domain.files.repository.OriginalFileInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FilesService {
    private final OriginalFileInfoRepository originalFileInfoRepository;
    private final EncryptedFileInfoRepository encryptedFileInfoRepository;
}
