package com.aladin.member.vo;

import java.time.LocalDateTime;

import com.aladin.member.enums.MemberGrade;
import com.aladin.member.enums.MemberStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberVo {
	private Long id; // DB 기본키
	private String username; // 아이디
	private String password; // 비밀번호
	private String name; // 실명
	private String nickname; // 별명
	private String email; // 이메일
	private String profileImagePath; // 프로필 사진 URL
	private MemberGrade grade; // 등급(NORMAL, BUSINESS, ADMIN)
	private MemberStatus status; // 상태(ACTIVE, SUSPENDED, DELETED)
	private LocalDateTime createdAt; // 회원 가입일
	private LocalDateTime lastjoinedAt; // 마지막 로그인
	private LocalDateTime deletedAt; // 회원 삭제일
	private String bio; // 소개글
}
