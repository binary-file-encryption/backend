package com.fescaro.binary_file_encryption.global.jwt.service;

import com.fescaro.binary_file_encryption.domain.user.entity.User;
import com.fescaro.binary_file_encryption.domain.user.repository.UserRepository;
import com.fescaro.binary_file_encryption.global.enums.statuscode.ErrorStatus;
import com.fescaro.binary_file_encryption.global.exception.GeneralException;
import com.fescaro.binary_file_encryption.global.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetails 객체 생성 후 반환
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 회원 객체 조회 후 반환
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
        return new CustomUserDetails(user);
    }
}
