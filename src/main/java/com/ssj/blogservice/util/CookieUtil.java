package com.ssj.blogservice.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {

    //요청 값(이름, 값, 만료기간)을 바탕으로 쿠키 추가 (로그인 성공 후, 토큰이나 유저정보를 쿠키에 담을 때 사용)
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value); // 쿠키 생성
        cookie.setPath("/");  // 모든 경로에서 접근 가능
        cookie.setMaxAge(maxAge);  // 쿠키 유효기간 설정

        response.addCookie(cookie); // 응답에 쿠키 추가
    }

    //쿠키의 이름을 입력받아 쿠키삭제 (로그아웃, 인증 해제 시)
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");  // 값 비우고
                cookie.setPath("/"); // 같은 경로 지정
                cookie.setMaxAge(0); // 만료 시간 0 → 즉시 삭제
                response.addCookie(cookie);  // 다시 클라이언트에게 전달
            }
        }
    }

    //개체를 직렬화해 쿠키의 값으로 저장 (바이트 배열 → Base64 문자열)
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    //쿠키를 역직렬화해 개체로 변환(로그인 유저 정보를 쿠키에 담았다가, 다시 꺼내서 User 객체로 변환)
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }

}
