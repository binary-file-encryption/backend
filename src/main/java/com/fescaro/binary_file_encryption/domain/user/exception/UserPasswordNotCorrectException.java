package com.fescaro.binary_file_encryption.domain.user.exception;

import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.exception.GeneralException;

public class UserPasswordNotCorrectException extends GeneralException {
    public UserPasswordNotCorrectException() {
        super(ErrorStatus._USER_PASSWORD_NOT_CORRECT);
    }
}
