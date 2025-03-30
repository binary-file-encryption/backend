package com.fescaro.binary_file_encryption.domain.files.controller;

import com.fescaro.binary_file_encryption.domain.files.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
public class FilesController {
    private final FilesService filesService;
}
