package com.fescaro.binary_file_encryption.domain.files.exception;

import com.fescaro.binary_file_encryption.global.enums.statuscode.BaseCode;
import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.exception.GeneralException;

public class EncryptFailException extends GeneralException {
    public EncryptFailException() {
        super(ErrorStatus._ENCRYPT_FAIL);
    }
}
