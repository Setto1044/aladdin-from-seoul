package com.aladin.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogInResponseDto {
	private String username;
	private String name;
	private String nickname;
	private String email;
	private String profileImagePath;
	private String grade;
	private String bio;
}
