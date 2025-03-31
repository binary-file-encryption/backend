package com.fescaro.binary_file_encryption.domain.files.service;

import com.fescaro.binary_file_encryption.domain.files.dto.FileResponseDto;
import com.fescaro.binary_file_encryption.domain.files.entity.EncryptedFileInfo;
import com.fescaro.binary_file_encryption.domain.files.entity.OriginalFileInfo;
import com.fescaro.binary_file_encryption.domain.files.exception.EncryptFailException;
import com.fescaro.binary_file_encryption.domain.files.repository.EncryptedFileInfoRepository;
import com.fescaro.binary_file_encryption.domain.files.repository.OriginalFileInfoRepository;
import com.fescaro.binary_file_encryption.domain.user.entity.User;
import com.fescaro.binary_file_encryption.domain.user.repository.UserRepository;
import com.fescaro.binary_file_encryption.global.encryption.aes.util.AESEncryptionUtil;
import com.fescaro.binary_file_encryption.global.storage.service.FileStorageService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FilesService {
    private final UserRepository userRepository;
    private final OriginalFileInfoRepository originalFileInfoRepository;
    private final EncryptedFileInfoRepository encryptedFileInfoRepository;
    private final FileStorageService fileStorageService;
    private final AESEncryptionUtil aesEncryptionUtil;

    @Transactional
    public FileResponseDto uploadAndEncrypt(String username, MultipartFile file) throws Exception {
        // 1. 현재 회원 정보 조회
        User user = userRepository.getByUsername(username);
        // 2. 원본 파일 저장
        String savedOriginalFileName = fileStorageService.uploadFile(file);
        // 3. 파일 암호화 및 저장소 저장
        File tempEncryptedFile = File.createTempFile("encrypted_", "_" + file.getOriginalFilename());
        String encryptionIV;
        try (InputStream in = file.getInputStream();
            OutputStream out = new FileOutputStream(tempEncryptedFile)) {
            encryptionIV = aesEncryptionUtil.encryptStream(in, out);
        } catch (Exception e) {
            throw new EncryptFailException(); // 파일 암호화 실패 예외 처리
        }
        // 4. 암호화된 파일 저장소 저장
        String encryptedOriginalFileName = "encrypted_" + file.getOriginalFilename();
        byte[] encryptedFileBytes = Files.readAllBytes(tempEncryptedFile.toPath());
        MultipartFile encryptedMultipartFile = new MockMultipartFile(
                "file",
                encryptedOriginalFileName,
                file.getContentType(),
                encryptedFileBytes
        );
        String savedEncryptedFileName = fileStorageService.uploadFile(encryptedMultipartFile);

        // 5. 암호화된 파일 엔티티 생성 및 저장
        EncryptedFileInfo encryptedFileInfoEntity
                = EncryptedFileInfo.toEntity(encryptedOriginalFileName, savedEncryptedFileName, encryptionIV);
        encryptedFileInfoRepository.save(encryptedFileInfoEntity);
        // 원본 파일 엔티티 생성 및 저장(+ 연관 관계 매핑)
        OriginalFileInfo originalFileInfoEntity
                = OriginalFileInfo.toEntity(user, file.getOriginalFilename(), savedOriginalFileName, encryptedFileInfoEntity);
        originalFileInfoRepository.save(originalFileInfoEntity);

        // 6. 임시 암호화 파일 삭제
        tempEncryptedFile.delete();

        // 7. 반환 DTO 작성 및 반환
        FileResponseDto fileResponseDto = FileResponseDto.of(originalFileInfoEntity, encryptedFileInfoEntity);
        return fileResponseDto;
    }
}
