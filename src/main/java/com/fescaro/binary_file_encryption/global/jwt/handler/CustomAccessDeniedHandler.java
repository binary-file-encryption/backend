package com.fescaro.binary_file_encryption.global.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

// 사용자에게 접근 권한이 없는 경우
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("No Authorities",accessDeniedException);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.onFailure(
                        ErrorStatus._TOKEN_NO_AUTHORIZATION,
                        "해당 사용자에겐 권한이 없습니다.")
        ));
    }

}
