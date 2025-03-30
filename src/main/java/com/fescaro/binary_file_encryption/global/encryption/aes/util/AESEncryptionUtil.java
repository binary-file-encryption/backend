package com.fescaro.binary_file_encryption.global.encryption.aes.util;

import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.CipherOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESEncryptionUtil {

    @Value("${encryption.secretKey}")
    private String secretKey; // AES 시크릿 키

    /**
     * AES-128 으로 파일 암호화
     * 입력 스트림(in)에서 데이터를 읽어, 출력 스트림(out)에 암호화된 데이터 기록
     * @param in  암호화할 데이터가 포함된 InputStream
     * @param out 암호화된 데이터를 기록할 OutputStream
     * @return 암호화에 사용된 IV (Base64 인코딩된 문자열)
     * @throws Exception
     */
    public String encryptStream(InputStream in, OutputStream out) throws Exception {
        // 랜덤 IV 생성 (16바이트)
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        // 비밀키 설정 (AES-128)
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 암호화 객체 생성
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        // 암호화 수행 -> 암호화 결과 OutputStream 에 작성
        try (CipherOutputStream cos = new CipherOutputStream(out, cipher)) {
            byte[] buffer = new byte[4096]; // 4096 바이트(4KB) 버퍼 사용
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }

        // 암호화에 사용된 IV값 반환
        return Base64.getEncoder().encodeToString(iv);
    }
}

