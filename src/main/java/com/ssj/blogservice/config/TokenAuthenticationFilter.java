package com.ssj.blogservice.config;

import com.ssj.blogservice.config.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter  {
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        //1. 요청 헤더의 AUTHORIZATION 키 값 꺼낸 후 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        //2. 가져온 값에서 "Bearer " 접두사 제거
        String token = getAccessToken(authorizationHeader);
        //3. 가져온 토큰이 유효한지 확인하고, 유효한때는 인증정보 설정
        if (tokenProvider.validToken(token)) {
            // 3-1. 토큰을 통해 사용자 정보(Authentication)를 꺼냄
            Authentication authentication = tokenProvider.getAuthentication(token);
            // 3-2. SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
