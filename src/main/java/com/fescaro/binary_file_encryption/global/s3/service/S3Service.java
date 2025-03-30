package com.fescaro.binary_file_encryption.global.s3.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.exception.GeneralException;
import com.fescaro.binary_file_encryption.global.s3.exception.S3ClientErrorException;
import com.fescaro.binary_file_encryption.global.s3.exception.S3FileNotFoundException;
import com.fescaro.binary_file_encryption.global.s3.exception.S3FileProcessingErrorException;
import com.fescaro.binary_file_encryption.global.s3.exception.S3RemoveFailException;
import com.fescaro.binary_file_encryption.global.s3.exception.S3UploadFailException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client s3Client;

    @Value("${s3.bucket}")
    private String bucket;

    /**
     * 파일 업로드
     */
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            // S3에 파일 업로드 시도
            s3Client.putObject(bucket, fileName, inputStream, metadata);
        } catch (AmazonServiceException e) {
            // S3와 관련된 오류 처리
            throw new S3UploadFailException();
        } catch (SdkClientException e) {
            // SDK 관련 오류 처리
            throw new S3ClientErrorException();
        } catch (IOException e) {
            // 파일 입출력 관련 오류 처리
            throw new S3FileProcessingErrorException();
        }

        // S3에 업로드된 파일 URL 반환
        URL fileUrl = s3Client.getUrl(bucket, fileName);
        return fileUrl.toString();
    }

    // S3주소로 파일 삭제
    public void deleteFileByS3URL(String s3URL) {
        if (s3URL != null && !s3URL.isEmpty()) {
            String fileName = extractFileNameFromUrl(s3URL);
            deleteFile(fileName);
        }
    }

    // 파일 삭제
    public void deleteFile(String fileName) {
        try {
            s3Client.deleteObject(bucket, fileName);
        } catch (AmazonServiceException e) {
            throw new S3RemoveFailException();
        } catch (SdkClientException e) {
            throw new S3ClientErrorException();
        }
    }

    // 파일 다운로드 메서드 - 파일이 존재하는지 확인한 후, 다운로드 가능한 바이트 배열 반환
    public byte[] downloadFile(String fileName) throws IOException {
        if (!s3Client.doesObjectExist(bucket, fileName)) {
            throw new S3FileNotFoundException(); // 파일이 존재하지 않는 경우
        }
        return s3Client.getObject(bucket, fileName).getObjectContent().readAllBytes();
    }

    // 파일 URL 반환 메서드 - 파일이 존재하는지 확인한 후, S3 링크를 반환
    public String getFileUrl(String fileName) {
        if (!s3Client.doesObjectExist(bucket, fileName)) {
            throw new S3FileNotFoundException(); // 파일이 존재하지 않는 경우
        }
        return s3Client.getUrl(bucket, fileName).toString();
    }

    // URL에서 파일명 추출
    private String extractFileNameFromUrl(String url) {
        try {
            // URL에서 파일명 부분만 추출 후, +를 공백으로 되돌리기
            String decodedUrl = java.net.URLDecoder.decode(url, "UTF-8");
            return decodedUrl.substring(decodedUrl.lastIndexOf("/") + 1);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }
}
