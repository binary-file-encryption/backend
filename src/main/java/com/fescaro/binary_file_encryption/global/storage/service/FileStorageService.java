package com.fescaro.binary_file_encryption.global.storage.service;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String uploadFile(MultipartFile file) throws IOException;

    // 스트림 기반 파일 업로드(대용량 파일 업로드에 용이)
    String uploadFileByStream(InputStream inputStream, long contentLength, String contentType, String originalFileName);
    void deleteFile(String fileName);
    byte[] downloadFile(String fileName) throws IOException;
}
