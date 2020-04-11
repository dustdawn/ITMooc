package com.njit.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author dustdawn
 * @date 2020/4/11 14:17
 */
public class Test {
    public static void main(String[] args) {
        String password = "111111";
        String encode = "$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(password, encode);
        System.out.println(matches);

        System.out.println(bCryptPasswordEncoder.encode("ITMooc"));
    }
}
