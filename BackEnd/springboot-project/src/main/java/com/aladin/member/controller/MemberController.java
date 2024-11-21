package com.aladin.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.common.ApiResponseDto;
import com.aladin.member.dto.LogInRequestDto;
import com.aladin.member.dto.LogInResponseDto;
import com.aladin.member.dto.MemberDeleteRequestDto;
import com.aladin.member.dto.MemberDeleteResponseDto;
import com.aladin.member.dto.MemberInfoResponseDto;
import com.aladin.member.dto.MemberRegistRequestDto;
import com.aladin.member.dto.MemberUpdateRequestDto;
import com.aladin.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/members")
public class MemberController {
	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		super();
		this.memberService = memberService;
	}

	@GetMapping("/{username}")
	public ResponseEntity<ApiResponseDto<MemberInfoResponseDto>> getMemberInfo(@PathVariable String username) {
		MemberInfoResponseDto memberInfo = memberService.getMemberInfo(username);
		return ResponseEntity.ok(ApiResponseDto.of(true, "회원 정보를 찾았습니다", memberInfo));
	}

	@PostMapping
	public ResponseEntity<ApiResponseDto<Void>> registerMember(@RequestBody MemberRegistRequestDto memberDto) {

		if (memberService.registMember(memberDto)) {
			return ResponseEntity.ok(ApiResponseDto.of(true, "성공적으로 회원가입되었습니다."));
		}
		return ResponseEntity.badRequest().body(ApiResponseDto.of(false, "회원가입에 실패했습니다."));
	}

	@GetMapping("/validate/username/{username}")
	public ResponseEntity<ApiResponseDto<Boolean>> validateUsername(@PathVariable String username) {
		boolean isDuplicated = memberService.isDuplicatedUsername(username);
		if (isDuplicated) {
			return ResponseEntity.ok(ApiResponseDto.of(false, "이미 사용 중인 아이디입니다."));
		}
		return ResponseEntity.ok(ApiResponseDto.of(true, "사용 가능한 아이디입니다."));
	}

	@GetMapping("/validate/email/{email}")
	public ResponseEntity<ApiResponseDto<Boolean>> validateEmail(@PathVariable String email) {
		boolean isDuplicated = memberService.isDuplicatedEmail(email);
		if (isDuplicated) {
			return ResponseEntity.ok(ApiResponseDto.of(false, "이미 사용 중인 이메일입니다."));
		}
		return ResponseEntity.ok(ApiResponseDto.of(true, "사용 가능한 이메일입니다."));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponseDto<LogInResponseDto>> loginMember(@RequestBody LogInRequestDto loginRequestDto) {
		LogInResponseDto member = memberService.loginMember(loginRequestDto);
		if (member != null) {
			return ResponseEntity.ok(ApiResponseDto.of(true, "로그인에 성공하였습니다.", member));
		}
		return ResponseEntity.badRequest().body(ApiResponseDto.of(false, "로그인에 실패했습니다."));
	}

	// 회원 정보 수정
	@PutMapping
	public ResponseEntity<ApiResponseDto<MemberInfoResponseDto>> updateMember(@ModelAttribute MemberUpdateRequestDto updateRequestDto) {
		try {
			MemberInfoResponseDto updatedMember = memberService.updateMember(updateRequestDto);
			return ResponseEntity.ok(ApiResponseDto.of(true, "회원 정보가 성공적으로 수정되었습니다.", updatedMember));
		} catch (Exception e) {
			log.error("회원 정보 수정 실패: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().body(ApiResponseDto.of(false, "회원 정보 수정에 실패했습니다."));
		}
	}

	@DeleteMapping
	public ResponseEntity<ApiResponseDto<MemberDeleteResponseDto>> deleteMember(@RequestBody MemberDeleteRequestDto requestDto) {
		boolean deleteResult = memberService.deleteMember(requestDto);
		if (deleteResult) {
			return ResponseEntity.ok(ApiResponseDto.of(deleteResult, "회원이 삭제되었습니다."));
		}
		return ResponseEntity.badRequest().body(ApiResponseDto.of(deleteResult, "회원 삭제에 실패했습니다."));
	}
}
