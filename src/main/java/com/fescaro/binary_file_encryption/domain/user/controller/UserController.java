package com.fescaro.binary_file_encryption.domain.user.controller;

import com.fescaro.binary_file_encryption.domain.user.dto.UserJoinReq;
import com.fescaro.binary_file_encryption.domain.user.dto.UserLoinReq;
import com.fescaro.binary_file_encryption.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody UserJoinReq userJoinReq) {
        return userService.join(userJoinReq);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoinReq userLoinReq) {
        return userService.login(userLoinReq);
    }
}
