package com.aladin.email.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.aladin.email.dto.EmailAuthRequestDto;
import com.aladin.email.dto.EmailRequestDto;
import com.aladin.email.mapper.EmailMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
	@Value("${spring.mail.username}")
	private String fromEmail;
	private final JavaMailSender mailSender;
	private final Map<String, EmailRequestDto> emailAuthCodeStorage = new HashMap<>();

	private final EmailMapper emailMapper;

	public EmailServiceImpl(JavaMailSender mailSender, EmailMapper emailMapper) {
		this.mailSender = mailSender;
		this.emailMapper = emailMapper;
	}

	@Override
	public boolean sendEmailAuthCode(EmailRequestDto emailRequestDto) {
		String email = emailRequestDto.getEmail();
		String username = emailRequestDto.getUsername();
		String authCode = generateAuthCode();

		if (!emailMapper.existsByEmailAndUsername(email, username)) {
			throw new IllegalArgumentException("일치하지 않는 회원 ID, 이메일입니다.");
		}

		emailRequestDto.setAuthExpireDate(LocalDateTime.now().plusMinutes(5));
		emailRequestDto.setAuthCode(authCode);
		emailAuthCodeStorage.put(email, emailRequestDto);

		try {
			sendEmail(email, authCode);
			return true;
		} catch (MessagingException e) {
			log.error("Failed to Send Email to: {}", email);
			return false;
		}
	}

	@Override
	public boolean verifyEmailAuthCode(EmailAuthRequestDto emailAuthRequestDto) {
		String toEmail = emailAuthRequestDto.getEmail();
		String inputAuthCode = emailAuthRequestDto.getAuthentication();
		EmailRequestDto storedRequest = emailAuthCodeStorage.get(toEmail);

		if (storedRequest == null) {
			return false;
		}
		if (LocalDateTime.now().isAfter(storedRequest.getAuthExpireDate())) {
			emailAuthCodeStorage.remove(toEmail);
			return false;
		}

		boolean isVerified = inputAuthCode.equals(storedRequest.getAuthCode());
		if (isVerified) {
			emailAuthCodeStorage.remove(toEmail);
		}
		return isVerified;
	}

	private String generateAuthCode() {
		int code = new Random().nextInt(1000000);
		return String.format("%06d", code);
	}

	private void sendEmail(String to, String authCode) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		// 이메일 설정
		helper.setFrom(fromEmail);
		helper.setTo(to);
		helper.setSubject("이메일 인증 코드");
		helper.setText("인증 코드:\n" + authCode);

		// 이메일 발송
		mailSender.send(message);
	}
}