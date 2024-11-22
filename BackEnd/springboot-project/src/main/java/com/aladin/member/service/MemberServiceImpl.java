package com.aladin.member.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aladin.common.ImageStorageMamager;
import com.aladin.common.ImageType;
import com.aladin.exceptions.BoardCreationException;
import com.aladin.exceptions.MemberNotFoundException;
import com.aladin.exceptions.ResourceNotFoundException;
import com.aladin.member.dto.LogInRequestDto;
import com.aladin.member.dto.LogInResponseDto;
import com.aladin.member.dto.MemberDeleteRequestDto;
import com.aladin.member.dto.MemberInfoResponseDto;
import com.aladin.member.dto.MemberPasswordUpdateRequestDto;
import com.aladin.member.dto.MemberRegistRequestDto;
import com.aladin.member.dto.MemberUpdateRequestDto;
import com.aladin.member.mapper.MemberMapper;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

	private final MemberMapper memberMapper;
	private final ImageStorageMamager imageStorageMamager;

	public MemberServiceImpl(MemberMapper memberMapper, ImageStorageMamager imageStorageMamager) {
		super();
		this.memberMapper = memberMapper;
		this.imageStorageMamager = imageStorageMamager;
	}

	@Override
	public boolean registMember(MemberRegistRequestDto memberDto) {
		if (memberMapper.isDuplicatedUsername(memberDto.getUsername())) {
			throw new BoardCreationException("이미 사용 중인 아이디입니다.");
		}
		if (memberMapper.isDuplicatedEmail(memberDto.getEmail())) {
			throw new BoardCreationException("이미 사용 중인 이메일입니다.");
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
		LogInResponseDto member = memberMapper.findMemberByUsernameAndPassword(loginRequestDto.getUsername(), loginRequestDto.getPassword());
		if (member == null) {
			throw new MemberNotFoundException("로그인 실패: 사용자 정보를 찾을 수 없습니다.");
		}
		return member;
	}

	@Override
	public MemberInfoResponseDto getMemberInfo(String username) {
		MemberInfoResponseDto memberInfo = memberMapper.findByUsername(username);
		if (memberInfo == null) {
			throw new MemberNotFoundException("사용자를 찾을 수 없습니다.");
		}
		return memberInfo;
	}

	@Override
	public MemberInfoResponseDto updateMember(MemberUpdateRequestDto updateRequestDto) {
		try {
			// 기존 회원 정보 확인
			MemberInfoResponseDto existingMember = memberMapper.findByUsername(updateRequestDto.getUsername());
			if (existingMember == null) {
				throw new ResourceNotFoundException("사용자를 찾을 수 없습니다.");
			}

			// 프로필 이미지 저장 또는 기본 이미지 설정
			String profileImagePath;
			if (updateRequestDto.getProfileImage() != null && !updateRequestDto.getProfileImage().isEmpty()) {
				profileImagePath = imageStorageMamager.saveImage(updateRequestDto.getProfileImage(), ImageType.PROFILE);
			} else {
				profileImagePath = existingMember.getProfileImagePath() != null ? existingMember.getProfileImagePath() : defaultProfileImageUrl();
			}
			updateRequestDto.setProfileImagePath(profileImagePath);

			// DB 업데이트
			int rowsAffected = memberMapper.updateMember(updateRequestDto);
			if (rowsAffected == 0) {
				throw new ResourceNotFoundException("회원 수정에 실패했습니다.");
			}

			return getMemberInfo(updateRequestDto.getUsername());
		} catch (IOException e) {
			throw new BoardCreationException("프로필 이미지 저장 중 오류가 발생했습니다.", e);
		}
	}

	/**
	 * 기본 프로필 이미지 URL 반환 수정 필요!!
	 */
	private String defaultProfileImageUrl() {
		return "http://localhost:8080/aladin/profile/defaultProfileImage.png";
	}

	@Override
	public void deleteMember(MemberDeleteRequestDto requestDto) {
		int rowsAffected = memberMapper.deleteMember(requestDto);
		if (rowsAffected == 0) {
			throw new ResourceNotFoundException("삭제할 회원 정보를 찾을 수 없습니다.");
		}
	}

	@Override
	public void updatePassword(MemberPasswordUpdateRequestDto memberPasswordUpdateRequestDto) {
		int rowsAffected = memberMapper.updatePassword(memberPasswordUpdateRequestDto);
		if (rowsAffected == 0) {
			throw new ResourceNotFoundException("비밀번호를 변경할 사용자를 찾을 수 없습니다.");
		}
	}

}
