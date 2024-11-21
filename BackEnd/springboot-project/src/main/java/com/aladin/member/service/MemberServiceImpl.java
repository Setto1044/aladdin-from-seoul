package com.aladin.member.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aladin.common.ImageStorageMamager;
import com.aladin.common.ImageType;
import com.aladin.member.dto.LogInRequestDto;
import com.aladin.member.dto.LogInResponseDto;
import com.aladin.member.dto.MemberInfoResponseDto;
import com.aladin.member.dto.MemberRegistRequestDto;
import com.aladin.member.dto.MemberUpdateRequestDto;
import com.aladin.member.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {

	private final MemberMapper memberMapper;
	private final ImageStorageMamager imageStorageManager;

	public MemberServiceImpl(MemberMapper memberMapper, ImageStorageMamager imageStorageManager) {
		this.memberMapper = memberMapper;
		this.imageStorageManager = imageStorageManager;
	}

	@Override
	public boolean registMember(MemberRegistRequestDto memberDto) {
		if (isDuplicatedEmail(memberDto.getEmail()) || isDuplicatedUsername(memberDto.getUsername())) {
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

	@Override
	public MemberInfoResponseDto updateMember(String username, MemberUpdateRequestDto updateRequestDto) {
		MultipartFile profileImage = updateRequestDto.getProfileImage();
		String profileImagePath = null;

		if (profileImage != null && !profileImage.isEmpty()) {
			try {
				// 이미지 저장
				profileImagePath = imageStorageManager.saveImage(profileImage, ImageType.PROFILE);
			} catch (IOException e) {
				throw new RuntimeException("프로필 이미지 저장 실패", e);
			}
		} else {
			// 기본 프로필 이미지 경로 설정
			profileImagePath = imageStorageManager.defaultProfileImagePath;
		}

		// 업데이트 DTO에 이미지 경로 추가
		updateRequestDto.setProfileImagePath(profileImagePath);

		// DB 업데이트
		int updatedRows = memberMapper.updateMember(updateRequestDto);
		if (updatedRows == 0) {
			throw new IllegalArgumentException("회원 정보 업데이트 실패");
		}

		// 수정된 회원 정보 반환
		return memberMapper.findByUsername(username);
	}

	@Override
	public MemberInfoResponseDto getMemberInfo(String username) {
		return memberMapper.findByUsername(username);
	}

}
