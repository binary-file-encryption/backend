package com.fescaro.binary_file_encryption.domain.files.entity;

import com.fescaro.binary_file_encryption.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "original_file_info")
public class OriginalFileInfo extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "original_file_info")
    private Long id;
    @Column(name = "file_name", nullable = false)
    private String fileName; // 원본 파일 이름
    @Column(name = "saved_file_name", nullable = false)
    private String savedFileName; // 저장소에 저장된 파일 이름 : UUID_원본 파일
}
