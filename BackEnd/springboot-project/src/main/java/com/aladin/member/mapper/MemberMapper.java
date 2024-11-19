package com.aladin.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.aladin.member.dto.LogInResponseDto;
import com.aladin.member.dto.MemberRegistRequestDto;

@Mapper
public interface MemberMapper {
	int registerMember(MemberRegistRequestDto member);
	boolean isDuplicatedUsername(String username);
	boolean isDuplicatedEmail(String email);
	LogInResponseDto findMemberByUsernameAndPassword(String username, String password);
}
