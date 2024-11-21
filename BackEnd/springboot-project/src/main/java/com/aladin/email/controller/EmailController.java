package com.aladin.email.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.email.dto.EmailAuthRequestDto;
import com.aladin.email.dto.EmailAuthResponseDto;
import com.aladin.email.dto.EmailRequestDto;
import com.aladin.email.dto.EmailSendResponseDto;
import com.aladin.email.service.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/email")
public class EmailController {
	private final EmailService emailService;

	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}

	@PostMapping
	public ResponseEntity<?> sendEmail(@RequestBody @Valid EmailRequestDto emailRequest) {
		boolean isOk = emailService.sendEmailAuthCode(emailRequest);
		EmailSendResponseDto emailSendResponseDto = EmailSendResponseDto.of(isOk);
		return ResponseEntity.ok(emailSendResponseDto);
	}

	@PostMapping("/authentication")
	public ResponseEntity<?> verifyAuthCode(@RequestBody @Valid EmailAuthRequestDto emailAuthRequest) {
		boolean isSuccess = emailService.verifyEmailAuthCode(emailAuthRequest);
		EmailAuthResponseDto emailAuthResponseDto = EmailAuthResponseDto.of(isSuccess);
		return ResponseEntity.ok(emailAuthResponseDto);
	}
}