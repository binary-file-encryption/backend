package com.fescaro.binary_file_encryption.domain.user.exception;

import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.exception.GeneralException;

public class UserNotFoundException extends GeneralException {
    public UserNotFoundException() {
        super(ErrorStatus._USER_NOT_FOUND);
    }
}
