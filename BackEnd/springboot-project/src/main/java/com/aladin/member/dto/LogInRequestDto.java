package com.aladin.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInRequestDto {
    private String username; // 회원 아이디
    private String password; // 비밀번호
}
