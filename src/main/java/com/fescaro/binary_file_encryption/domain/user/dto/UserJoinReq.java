package com.fescaro.binary_file_encryption.domain.user.dto;

import com.fescaro.binary_file_encryption.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;

public record UserJoinReq(
        @NotNull(message = "username은 필수입니다.")
        String username,
        @NotNull(message = "password는 필수입니다.")
        String password
) {
        public static User of(String username, String password) {
                return User.builder()
                        .username(username)
                        .password(password)
                        .role("ROLE_USER")
                        .build();
        }
}
