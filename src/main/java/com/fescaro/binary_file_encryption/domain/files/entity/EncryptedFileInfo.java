package com.fescaro.binary_file_encryption.domain.files.entity;

import com.fescaro.binary_file_encryption.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.security.Principal;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "encrypted_file_info")
public class EncryptedFileInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "encrypted_file_id")
    private Long id;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "saved_file_name", nullable = false)
    private String savedFileName;
    @Column(name = "iv_value", nullable = false)
    private String ivValue;

    @OneToOne
    @JoinColumn(name = "original_file_id", nullable = false)
    private OriginalFileInfo originalFileInfo;


    // == 편의 메소드 == //
    public static EncryptedFileInfo toEntity(String fileName, String savedFileName, String ivValue) {
        return EncryptedFileInfo.builder()
                .fileName(fileName)
                .savedFileName(savedFileName)
                .ivValue(ivValue)
                .build();
    }
}
