package com.fescaro.binary_file_encryption.domain.files.controller;

import com.fescaro.binary_file_encryption.domain.files.dto.FileResponseDto;
import com.fescaro.binary_file_encryption.domain.files.service.FilesService;
import com.fescaro.binary_file_encryption.global.jwt.dto.CustomUserDetails;
import com.fescaro.binary_file_encryption.global.response.ApiResponse;
import com.fescaro.binary_file_encryption.global.storage.service.FileStorageService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
public class FilesController {
    private final FilesService filesService;
    private final FileStorageService fileStorageService;

    /**
     * 파일 암호화 및 업로드
     */
    @PostMapping("/upload-and-encrypt")
    public ResponseEntity<?> uploadAndEncrypt(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception {
        FileResponseDto fileResponseDto = filesService.uploadAndEncrypt(customUserDetails.getUsername(), file);
        return ResponseEntity.ok(ApiResponse.onSuccess(fileResponseDto));
    }

    /**
     * 사용자가 업로드한 파일목록 조회
     */
    @GetMapping("/mine")
    public ResponseEntity<?> getMyFiles(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        List<FileResponseDto> myFiles = filesService.getMyFiles(customUserDetails.getUsername(), page, size);
        return ResponseEntity.ok(ApiResponse.onSuccess(myFiles));
    }

    /**
     * 파일 다운로드 링크 제공
     * @param name
     * @return
     * @throws IOException
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("name") String name) throws IOException {
        byte[] fileData = fileStorageService.downloadFile(name);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData);
    }
}
