package com.aladin.member.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class MemberRegisterResponseDto {
	private boolean isSuccess;

	private MemberRegisterResponseDto(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public static MemberRegisterResponseDto of(boolean isSuccess) {
		return new MemberRegisterResponseDto(isSuccess);
	}
}
