package com.fescaro.binary_file_encryption.domain.files.controller;

import com.fescaro.binary_file_encryption.domain.files.dto.FileResponseDto;
import com.fescaro.binary_file_encryption.domain.files.service.FilesService;
import com.fescaro.binary_file_encryption.global.jwt.dto.CustomUserDetails;
import com.fescaro.binary_file_encryption.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
public class FilesController {
    private final FilesService filesService;

    @PostMapping("/upload-and-encrypt")
    public ResponseEntity<?> uploadAndEncrypt(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception {
        FileResponseDto fileResponseDto = filesService.uploadAndEncrypt(customUserDetails.getUsername(), file);
        return ResponseEntity.ok(ApiResponse.onSuccess(fileResponseDto));
    }
}
