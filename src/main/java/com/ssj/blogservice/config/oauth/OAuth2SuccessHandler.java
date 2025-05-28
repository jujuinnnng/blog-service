package com.ssj.blogservice.config.oauth;

import com.ssj.blogservice.config.jwt.TokenProvider;
import com.ssj.blogservice.domain.RefreshToken;
import com.ssj.blogservice.domain.User;
import com.ssj.blogservice.repository.RefreshTokenRepository;
import com.ssj.blogservice.service.UserService;
import com.ssj.blogservice.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // Refresh Token 관련 상수 설정
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";  // 쿠키에 저장될 Refresh Token의 이름
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);  // Refresh Token 유효 기간 (14일)
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);  // Access Token 유효 기간 (1일)
    public static final String REDIRECT_PATH = "/articles";  // 리다이렉션 경로

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // OAuth2 인증이 성공하면 사용자 정보를 가져옴
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));

        // Refresh Token 생성 후 저장 및 쿠키에 추가
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        // Access Token 생성 후 리다이렉션 URL에 포함
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken);

        // 인증 관련 속성 제거
        clearAuthenticationAttributes(request, response);

        // 사용자를 지정된 URL로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // 생성된 Refresh Token을 저장 또는 업데이트
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))  // 기존 토큰 업데이트
                .orElse(new RefreshToken(userId, newRefreshToken));  // 새로 저장

        refreshTokenRepository.save(refreshToken);  // DB에 저장
    }

    // 생성된 Refresh Token을 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();  // 쿠키 유효 기간 설정

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);  // 기존 쿠키 삭제
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);  // 새 쿠키 추가
    }

    // OAuth2 인증 관련 속성 제거 (보안 목적)
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);  // 기본 인증 속성 제거
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);  // 인증 요청 쿠키 제거
    }

    // 리다이렉트될 URL 생성, Access Token을 쿼리 파라미터로 포함
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}