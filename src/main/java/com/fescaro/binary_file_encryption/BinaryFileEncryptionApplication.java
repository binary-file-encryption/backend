package com.fescaro.binary_file_encryption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Entity Listener available
public class BinaryFileEncryptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(BinaryFileEncryptionApplication.class, args);
	}

}
