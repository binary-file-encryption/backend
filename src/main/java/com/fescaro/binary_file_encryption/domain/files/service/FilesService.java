package com.fescaro.binary_file_encryption.domain.files.service;

import com.fescaro.binary_file_encryption.domain.files.dto.FileResponseDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    FileResponseDto uploadAndEncrypt(String username, MultipartFile file) throws Exception;
    List<FileResponseDto> getMyFiles(String username, int page, int size);
}
