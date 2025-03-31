package com.fescaro.binary_file_encryption.domain.files.service;

import com.fescaro.binary_file_encryption.domain.files.dto.FileResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    FileResponseDto uploadAndEncrypt(String username, MultipartFile file) throws Exception;
}
