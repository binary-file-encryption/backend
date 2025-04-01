package com.fescaro.binary_file_encryption.global.encryption.aes.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest
public class AESEncryptionUtilTest {

    @Autowired
    private AESEncryptionUtil aesEncryptionUtil;

    @Value("${encryption.secretKey}")
    private String secretKey; // AES 시크릿 키

    @Test
    public void 바이너리_파일_암호화_테스트() throws Exception {
        // Arrange
        // 테스트용 바이너리 파일 읽어오기 (src/test/resources/test.bin)
        ClassPathResource resource = new ClassPathResource("test.bin");
        byte[] originalData;
        try (InputStream originalIn = resource.getInputStream()) {
            originalData = originalIn.readAllBytes();
        }

        // 암호화 결과를 저장할 출력 스트림 생성
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Act
        // 바이너리 파일 암호화 수행
        String ivBase64;
        try (InputStream in = resource.getInputStream()) {
            ivBase64 = aesEncryptionUtil.encryptStream(in, out);
        }
        byte[] encryptedData = out.toByteArray();

        // Assert
        // IV 반환 검증
        assertNotNull(ivBase64, "IV는 null이면 안됩니다.");
        assertFalse(ivBase64.isEmpty(), "IV는 empty면 안됩니다.");

        // 암호화된 데이터가 존재하는지 확인
        assertNotNull(encryptedData, "암호화된 데이터는 null이면 안됩니다.");
        assertTrue(encryptedData.length > 0, "암호화된 데이터의 길인는 0보다 커야합니다.");

        // 원본 데이터와 암호화된 데이터는 달라야 함
        assertFalse(Arrays.equals(originalData, encryptedData), "암호화된 결과물은 원본과 달라야 합니다.ㄴ");
    }


    @Test
    public void 바이너리_파일_암호화_복호화_테스트() throws Exception {
        // Arrange
        // 테스트용 파일 읽어오기
        ClassPathResource resource = new ClassPathResource("test.bin");
        byte[] originalData;
        try (InputStream originalIn = resource.getInputStream()) {
            originalData = originalIn.readAllBytes();
        }

        // 암호화 결과를 저장할 출력 스트림 생성
        ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();

        // Act 1: 바이너리 파일 암호화 수행
        String ivBase64;
        try (InputStream in = resource.getInputStream()) {
            ivBase64 = aesEncryptionUtil.encryptStream(in, encryptedOut);
        }
        byte[] encryptedData = encryptedOut.toByteArray();

        // Act 2: 암호화된 데이터 복호화
        byte[] decryptedData = decrypt(encryptedData, ivBase64);

        // Assert
        // 복호화된 데이터가 원본 데이터와 일치하는지 확인
        assertArrayEquals(originalData, decryptedData, "복호화된 데이터가 원본과 일치해야 합니다.");
    }

    /**
     * 암호화된 데이터 복호화 수행
     */
    private byte[] decrypt(byte[] encryptedData, String ivBase64) throws Exception {
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        // Cipher 객체 생성 및 복호화 모드로 초기화 (AES/CBC/PKCS5Padding)
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        return cipher.doFinal(encryptedData);
    }
}
