package com.aladin.member.dto;

import com.aladin.member.enums.MemberGrade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberRegistRequestDto {
	private String username;	// 아이디
	private String password;	// 비밀번호
	private String name;		// 실명
	private String nickname;	// 별명
	private String email;		// 이메일
	private MemberGrade grade;	// 등급(일반, 기업 관리자 등)
}
