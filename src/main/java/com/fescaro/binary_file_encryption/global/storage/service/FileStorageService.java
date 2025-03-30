package com.fescaro.binary_file_encryption.global.storage.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    void uploadFile(MultipartFile file) throws IOException;
    void deleteFile(String fileName);
    byte[] downloadFile(String fileName) throws IOException;
}
