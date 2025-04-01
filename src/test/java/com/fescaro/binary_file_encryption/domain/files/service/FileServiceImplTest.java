package com.fescaro.binary_file_encryption.domain.files.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fescaro.binary_file_encryption.domain.files.dto.FileResponseDto;
import com.fescaro.binary_file_encryption.domain.files.entity.EncryptedFileInfo;
import com.fescaro.binary_file_encryption.domain.files.entity.OriginalFileInfo;
import com.fescaro.binary_file_encryption.domain.user.entity.User;
import com.fescaro.binary_file_encryption.domain.user.repository.UserRepository;
import com.fescaro.binary_file_encryption.domain.files.repository.OriginalFileInfoRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private OriginalFileInfoRepository originalFileInfoRepository;
    @InjectMocks
    private FileServiceImpl fileService;

    @Test
    public void 파일_조회_테스트() {
        // given
        String username = "testUser";
        User user = User.builder().username(username).build();
        when(userRepository.getByUsername(eq(username))).thenReturn(user);

        // 조회 성공 확인용 더미 데이터 작성
        OriginalFileInfo originalFile = OriginalFileInfo.builder()
                .id(1L)
                .fileName("file1.txt")
                .savedFileName("uuid_file1.txt")
                .user(user)
                .build();
        EncryptedFileInfo encryptedFile = EncryptedFileInfo.builder()
                .id(101L)
                .fileName("encrypted_file1.txt")
                .savedFileName("uuid_encrypted_file1.txt")
                .ivValue("randomIV")
                .build();
        originalFile.setMapping(encryptedFile);

        Page<OriginalFileInfo> page = new PageImpl<>(Arrays.asList(originalFile));
        when(originalFileInfoRepository.getMyFiles(eq(user), any(Pageable.class))).thenReturn(page);

        // when
        // 파일 조회 결과 획득
        List<FileResponseDto> files = fileService.getMyFiles(username, 0, 5);

        // then
        assertNotNull(files);
        assertEquals(1, files.size());
        FileResponseDto dto = files.get(0);
        assertEquals("file1.txt", dto.originalFileName());
        assertEquals("encrypted_file1.txt", dto.encryptedFileName());
        assertEquals("randomIV", dto.ivValue());
    }
}