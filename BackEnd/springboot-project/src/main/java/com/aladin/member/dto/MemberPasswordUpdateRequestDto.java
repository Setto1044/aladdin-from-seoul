package com.aladin.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberPasswordUpdateRequestDto {
	private String username;
	private String password;
}
