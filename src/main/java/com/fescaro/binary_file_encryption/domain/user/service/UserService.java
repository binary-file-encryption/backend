package com.fescaro.binary_file_encryption.domain.user.service;

import com.fescaro.binary_file_encryption.domain.user.dto.UserJoinReq;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> join(UserJoinReq userJoinReq);
}
