package com.example.miniproject;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaAuditing
public class MiniProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniProjectApplication.class, args);

//		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//		encryptor.setProvider(new BouncyCastleProvider());
//		encryptor.setPoolSize(2);
//		encryptor.setPassword("030c37f1d9dbe200ce7cc010b68e9e88ab7fe7621df20d17c29299ce3ccfb1faa2cf83a27f608605e7d29fcc3e56866e5d24a9fc8c7b584892c702c12e0c21e3");
//		encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");
//
//		String plainText = "kctVuyScFI4bVvCpRFDvhdtdBV1v3eaZUMbxBppE";
//		String encryptedText = encryptor.encrypt(plainText);
//		String decryptedText = encryptor.decrypt(encryptedText);
//		System.out.println("Enc = " + encryptedText);
//		System.out.println("Dec = " + decryptedText);



	}
}
