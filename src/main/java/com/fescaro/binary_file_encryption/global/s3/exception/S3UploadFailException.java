package com.fescaro.binary_file_encryption.global.s3.exception;

import com.fescaro.binary_file_encryption.global.enums.statuscode.BaseCode;
import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.exception.GeneralException;

public class S3UploadFailException extends GeneralException {
    public S3UploadFailException() {
        super(ErrorStatus._S3_UPLOAD_FAIL);
    }
}
