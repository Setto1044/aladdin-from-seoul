package com.aladin.member.dto;

import com.aladin.member.enums.MemberGrade;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfoResponseDto {
	private String username;
	private String name;
	private String nickname;
	private String email;
	private String profileImagePath;
	private String bio;
	private MemberGrade grade;
}
