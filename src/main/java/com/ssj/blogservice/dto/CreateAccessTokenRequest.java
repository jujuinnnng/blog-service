package com.ssj.blogservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenRequest { //토큰 생성 요청 클래스
    private String refreshToken;
}
