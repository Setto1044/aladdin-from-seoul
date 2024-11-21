package com.aladin.member.dto;

import lombok.Data;

@Data
public class MemberDeleteResponseDto {
	private boolean isSuccess;

	private MemberDeleteResponseDto(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public static MemberDeleteResponseDto of(boolean isSuccess) {
		return new MemberDeleteResponseDto(isSuccess);
	}
}
