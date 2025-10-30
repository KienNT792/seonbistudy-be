package com.seonbistudy.seonbistudy.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String defaultPassword = "password123";
        System.out.println("--- BCRYPT HASHES ---");
        System.out.println(passwordEncoder.encode(defaultPassword));
    }
}
