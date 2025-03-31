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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
    @Override
    public FileResponseDto uploadAndEncrypt(String username, MultipartFile file) throws Exception {
        // 1. 현재 회원 정보 조회
        User user = userRepository.getByUsername(username);
        // 2. 원본 파일 저장소에 저장
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
        String savedEncryptedFileName;
        // stream 기반 저장소 파일 업로드
        try (InputStream encryptedFileInputStream = new FileInputStream(tempEncryptedFile)) {
            savedEncryptedFileName = fileStorageService.uploadFileByStream(
                    encryptedFileInputStream,
                    tempEncryptedFile.length(),
                    file.getContentType(),
                    encryptedOriginalFileName
            );
        }
        // 5. 원본 파일 엔티티 생성 및 저장
        OriginalFileInfo originalFileInfoEntity = OriginalFileInfo.toEntity(
                user,
                file.getOriginalFilename(),
                savedOriginalFileName);
        originalFileInfoRepository.save(originalFileInfoEntity);

        // 6. 암호화된 파일 엔티티 생성 및 연관관계 설정
        EncryptedFileInfo encryptedFileInfoEntity = EncryptedFileInfo.toEntity(
                encryptedOriginalFileName,
                savedEncryptedFileName,
                encryptionIV
        );
        // 연관 관계 매핑
        originalFileInfoEntity.setMapping(encryptedFileInfoEntity);
        encryptedFileInfoRepository.save(encryptedFileInfoEntity);
        originalFileInfoRepository.save(originalFileInfoEntity);

        // 7. 임시 암호화 파일 삭제
        tempEncryptedFile.delete();

        // 8. 반환 DTO 작성 및 반환
        FileResponseDto fileResponseDto = FileResponseDto.of(originalFileInfoEntity, encryptedFileInfoEntity);
        return fileResponseDto;
    }

    /**
     * 사용자가 업로드한 파일 조회(페이지네이션)
     * @param username
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<FileResponseDto> getMyFiles(String username, int page, int size) {
        // 1. 사용자 정보 조회
        User user = userRepository.getByUsername(username);
        // 2. 사용자가 업로드한 파일 정보 조회(페이지네이션)
        // 정렬 기준 설정
        PageRequest pageRequest = PageRequest.of(page, size);
        Pageable pageable = PageRequest.of(
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                Sort.by(Direction.DESC, "createdAt")); // 생성 시각을 기준으로 정렬

        Page<OriginalFileInfo> myFiles = originalFileInfoRepository.getMyFiles(user, pageable);
        // 3. 반환 DTO 작성 및 반환
        ArrayList<FileResponseDto> responseDtoArrayList = new ArrayList<>();
        for (OriginalFileInfo originalFileInfo : myFiles.getContent()) {
            responseDtoArrayList.add(FileResponseDto.of(originalFileInfo, originalFileInfo.getEncryptedFileInfo()));
        }
        return responseDtoArrayList;
    }

}
