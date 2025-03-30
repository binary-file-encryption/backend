package com.fescaro.binary_file_encryption.global.s3.exception;

import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.exception.GeneralException;

public class S3FileNotFoundException extends GeneralException {
    public S3FileNotFoundException() {
        super(ErrorStatus._S3_FILE_NOT_FOUND);
    }
}
