package com.aladin.member.service;

import com.aladin.member.dto.LogInRequestDto;
import com.aladin.member.dto.LogInResponseDto;
import com.aladin.member.dto.MemberInfoResponseDto;
import com.aladin.member.dto.MemberRegistRequestDto;
import com.aladin.member.dto.MemberUpdateRequestDto;

public interface MemberService {
	boolean registMember(MemberRegistRequestDto memberDto);

	boolean isDuplicatedUsername(String username);

	boolean isDuplicatedEmail(String email);

	LogInResponseDto loginMember(LogInRequestDto loginRequestDto);

	MemberInfoResponseDto updateMember(String username, MemberUpdateRequestDto updateRequestDto);

	MemberInfoResponseDto getMemberInfo(String username);
}
