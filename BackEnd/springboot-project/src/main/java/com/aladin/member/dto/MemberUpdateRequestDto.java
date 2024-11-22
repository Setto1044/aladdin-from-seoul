package com.aladin.member.dto;

import org.springframework.web.multipart.MultipartFile;

import com.aladin.member.enums.MemberGrade;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberUpdateRequestDto {
	private String username; // 검증용
	private String nickname; // 별명
	private MultipartFile profileImage; // 프로필 사진 파일
	private String profileImagePath; // 프로필 URL
	private String bio; // 소개글
	private MemberGrade grade; // 등급
}
