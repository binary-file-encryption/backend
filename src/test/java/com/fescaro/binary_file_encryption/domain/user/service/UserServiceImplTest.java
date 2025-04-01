package com.fescaro.binary_file_encryption.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.fescaro.binary_file_encryption.domain.user.dto.UserJoinReq;
import com.fescaro.binary_file_encryption.domain.user.dto.UserLoinReq;
import com.fescaro.binary_file_encryption.domain.user.entity.User;
import com.fescaro.binary_file_encryption.domain.user.exception.UserPasswordNotCorrectException;
import com.fescaro.binary_file_encryption.domain.user.repository.UserRepository;
import com.fescaro.binary_file_encryption.global.jwt.util.JwtUtil;
import com.fescaro.binary_file_encryption.global.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void 회원가입_테스트() throws Exception {
        // given
        String username = "testUser";
        String password = "testPassword";
        String encodedPassword = "encodedTestPassword";
        UserJoinReq userJoinReq = new UserJoinReq(username, password);

        // 동일 username이 존재하지 않음
        Mockito.when(userRepository.existsByUsername(eq(username))).thenReturn(false);
        // 비밀번호 암호화 확인
        Mockito.when(passwordEncoder.encode(eq(password))).thenReturn(encodedPassword);

        // 회원 저장 시 반환될 User 객체 생성
        User savedUser = makeUser(username,password);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // JWT 생성
        String token = "dummyToken";
        Mockito.when(jwtUtil.createJwt(eq(username), any())).thenReturn(token);

        // when
        ResponseEntity<?> responseEntity = userService.join(userJoinReq);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("Bearer " + token, apiResponse.getResult());
    }

    @Test
    public void 로그인_테스트_성공() {
        // given
        String username = "testUser";
        String rawPassword = "testPassword";
        String encodedPassword = "encodedTestPassword";
        UserLoinReq req = new UserLoinReq(username, rawPassword);

        // 로그인 테스트용 객체
        User user = makeUser(username, encodedPassword);

        // 회원 조회, 비밀번호 매칭, JWT 생성 모킹
        Mockito.when(userRepository.getByUsername(eq(username))).thenReturn(user);
        Mockito.when(passwordEncoder.matches(eq(rawPassword), eq(encodedPassword))).thenReturn(true);
        String token = "dummyToken";
        Mockito.when(jwtUtil.createJwt(eq(username), any())).thenReturn(token);

        // when
        ResponseEntity<?> responseEntity = userService.login(req);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("Bearer " + token, apiResponse.getResult());
    }


    @Test
    public void 로그인_테스트_실패_잘못된비밀번호() {
        // given
        String username = "testUser";
        String rawPassword = "wrongPassword";
        String encodedPassword = "encodedTestPassword";
        UserLoinReq req = new UserLoinReq(username, rawPassword);

        // 로그인 테스트용 객체
        User user = makeUser(username, encodedPassword);

        Mockito.when(userRepository.getByUsername(eq(username))).thenReturn(user);
        // 비밀번호 매칭 실패 모킹
        Mockito.when(passwordEncoder.matches(eq(rawPassword), eq(encodedPassword))).thenReturn(false);

        // when & then: 잘못된 비밀번호 입력 시 UserPasswordNotCorrectException 발생 검증
        assertThrows(UserPasswordNotCorrectException.class, () -> userService.login(req));
    }

    public User makeUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .role("ROLE_USER")
                .build();
    }
}
