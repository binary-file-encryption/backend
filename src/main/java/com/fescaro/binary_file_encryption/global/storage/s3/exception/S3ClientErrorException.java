package com.fescaro.binary_file_encryption.global.storage.s3.exception;

import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.exception.GeneralException;

public class S3ClientErrorException extends GeneralException {
    public S3ClientErrorException() {
        super(ErrorStatus._S3_CLIENT_ERROR);
    }
}
