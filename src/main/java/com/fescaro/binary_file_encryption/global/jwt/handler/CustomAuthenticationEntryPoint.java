package com.fescaro.binary_file_encryption.global.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// 사용자가 JWT를 삽입하지 않았거나, 삽입한 JWT가 유효하지 않은 경우에 발생
@Slf4j(topic = "UNAUTHORIZATION_EXCEPTION_HANDLER")
@Component
@AllArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        log.error("Not Authenticated Request",authException);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.onFailure(
                        ErrorStatus._TOKEN_ERROR,
                        "1. JWT를 다시 한번 확인해주세요.(유효기간, Bearer, 등), 2. API 명세서의 요구사항을 모두 지켰는지 확인해주세요(DTO오타, 엔드포인트, etc)")
        ));
    }
}
