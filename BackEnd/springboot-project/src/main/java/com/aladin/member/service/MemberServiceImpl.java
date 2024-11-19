package com.aladin.member.service;

import org.springframework.stereotype.Service;

import com.aladin.member.dto.LogInRequestDto;
import com.aladin.member.dto.LogInResponseDto;
import com.aladin.member.dto.MemberRegistRequestDto;
import com.aladin.member.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService{

	private final MemberMapper memberMapper;
	
	public MemberServiceImpl(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	
	@Override
	public boolean registMember(MemberRegistRequestDto memberDto) {
		if( isDuplicatedEmail(memberDto.getEmail()) || isDuplicatedUsername(memberDto.getUsername())) {
			return false;
		}
		return memberMapper.registerMember(memberDto) > 0;
	}

	@Override
	public boolean isDuplicatedUsername(String username) {
		return memberMapper.isDuplicatedUsername(username);
	}

	@Override
	public boolean isDuplicatedEmail(String email) {
		return memberMapper.isDuplicatedEmail(email);
	}

	@Override
	public LogInResponseDto loginMember(LogInRequestDto loginRequestDto) {
		return memberMapper.findMemberByUsernameAndPassword(loginRequestDto.getUsername(), loginRequestDto.getPassword());
	}

}
