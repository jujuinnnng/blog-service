package com.ssj.blogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CreateAccessTokenResponse { //토큰 생성 응답 클래스
    private String accessToken;
}
