package com.fescaro.binary_file_encryption.domain.user.dto;

import jakarta.validation.constraints.NotNull;

public record UserJoinReq(
        @NotNull(message = "username은 필수입니다.")
        String username,
        @NotNull(message = "password는 필수입니다.")
        String password
) {
}
