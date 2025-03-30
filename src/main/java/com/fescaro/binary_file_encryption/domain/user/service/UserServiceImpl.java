package com.fescaro.binary_file_encryption.domain.user.service;

import com.fescaro.binary_file_encryption.domain.user.dto.UserJoinReq;
import com.fescaro.binary_file_encryption.domain.user.dto.UserLoinReq;
import com.fescaro.binary_file_encryption.domain.user.entity.User;
import com.fescaro.binary_file_encryption.domain.user.exception.UserPasswordNotCorrectException;
import com.fescaro.binary_file_encryption.domain.user.repository.UserRepository;
import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.jwt.util.JwtUtil;
import com.fescaro.binary_file_encryption.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원 가입
     */
    @Override
    public ResponseEntity<?> join(UserJoinReq userJoinReq) {
        // 동일 username 사용자 생성 방지
        if (userRepository.existsByUsername(userJoinReq.username())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.onFailure(ErrorStatus._USER_IS_EXISTS, "회원가입에 실패하였습니다."));
        }
        String encodedPassword = passwordEncoder.encode(userJoinReq.password());
        User user = UserJoinReq.of(userJoinReq.username(), encodedPassword);
        userRepository.save(user);
        return getJwtResponseEntity(user);
    }

    /**
     * 로그인
     */
    @Override
    public ResponseEntity<?> login(UserLoinReq userLoinReq) {
        // 회원 엔티티 조회
        User user = userRepository.getByUsername(userLoinReq.username());
        // 비밀 번호 검증
        if(!passwordEncoder.matches(userLoinReq.password(), user.getPassword())) {
            throw new UserPasswordNotCorrectException();
        }
        return getJwtResponseEntity(user);
    }

    // 회원 가입 & 로그인 성공시 JWT 생성 후 반환
    public ResponseEntity<?> getJwtResponseEntity(User user) {
        String accessToken = jwtUtil.createJwt(user.getUsername(), user.getRole());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        return ResponseEntity.ok().headers(headers)
                .body(ApiResponse.onSuccess("Bearer " + accessToken));
    }
}
