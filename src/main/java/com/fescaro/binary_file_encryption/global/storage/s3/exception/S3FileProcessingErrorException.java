package com.fescaro.binary_file_encryption.global.storage.s3.exception;

import com.fescaro.binary_file_encryption.global.enums.statuscode.BaseCode;
import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.exception.GeneralException;

public class S3FileProcessingErrorException extends GeneralException {
    public S3FileProcessingErrorException() {
        super(ErrorStatus._S3_FILE_PROCESSING_ERROR);
    }
}
