package com.aladin.member.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberUpdateRequestDto {
	private String username;
	private String password; // 비밀번호
	private String name; // 실명
	private String nickname; // 별명
	private MultipartFile profileImage; // 프로필 사진 파일
	private String profileImagePath;
	private String bio; // 소개글
}
