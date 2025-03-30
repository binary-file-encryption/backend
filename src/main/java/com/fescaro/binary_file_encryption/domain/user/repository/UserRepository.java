package com.fescaro.binary_file_encryption.domain.user.repository;

import com.fescaro.binary_file_encryption.domain.user.entity.User;
import com.fescaro.binary_file_encryption.domain.user.exception.UserNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    default User getByUsername(String username) {
        return findByUsername(username).orElseThrow(() -> new UserNotFoundException());
    }

    Boolean existsByUsername(String username);
}
