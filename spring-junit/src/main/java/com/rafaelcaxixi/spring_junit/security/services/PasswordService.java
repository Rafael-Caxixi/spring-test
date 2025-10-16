package com.rafaelcaxixi.spring_junit.security.services;

public interface PasswordService {
    String encryptPassword(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
