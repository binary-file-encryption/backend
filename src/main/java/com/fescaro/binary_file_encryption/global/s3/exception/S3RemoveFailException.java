package com.fescaro.binary_file_encryption.global.s3.exception;

import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.exception.GeneralException;

public class S3RemoveFailException extends GeneralException {
    public S3RemoveFailException() {
        super(ErrorStatus._S3_REMOVE_FAIL);
    }
}
