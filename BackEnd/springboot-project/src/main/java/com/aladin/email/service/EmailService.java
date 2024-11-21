package com.aladin.email.service;

import com.aladin.email.dto.EmailAuthRequestDto;
import com.aladin.email.dto.EmailRequestDto;

public interface EmailService {
	boolean sendEmailAuthCode(EmailRequestDto emailRequestDto);

	boolean verifyEmailAuthCode(EmailAuthRequestDto emailAuthRequestDto);
}
