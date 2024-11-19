package com.aladin.member.service;

import com.aladin.member.dto.LogInRequestDto;
import com.aladin.member.dto.LogInResponseDto;
import com.aladin.member.dto.MemberRegistRequestDto;

public interface MemberService {
	boolean registMember(MemberRegistRequestDto memberDto);
	boolean isDuplicatedUsername(String username);
	boolean isDuplicatedEmail(String email);
	LogInResponseDto loginMember(LogInRequestDto loginRequestDto);
	
}
