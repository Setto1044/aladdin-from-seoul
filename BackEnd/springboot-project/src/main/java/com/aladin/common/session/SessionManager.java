package com.aladin.common.session;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// WebConfig에 세션 검증 요청 URL 구성
@Component
public class SessionManager {

	private static final String SESSION_COOKIE_NAME = "SESSIONID";

	// 세션 생성 및 쿠키 설정
	public void createSession(HttpServletRequest request, HttpServletResponse response, String username) {
		HttpSession session = request.getSession(true); // 세션 생성
		session.setAttribute(SESSION_COOKIE_NAME, username);

		// 세션 쿠키 생성
		Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, session.getId());
		sessionCookie.setHttpOnly(true); // 클라이언트 JS 접근 불가
		sessionCookie.setPath("/"); // 모든 경로에 대해 유효
		response.addCookie(sessionCookie);
	}

	// 세션에서 사용자 이름 반환
	public String getSessionUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return (session != null) ? (String) session.getAttribute(SESSION_COOKIE_NAME) : null;
	}

	// 세션 삭제 및 쿠키 제거
	public void invalidateSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, null);
		cookie.setMaxAge(0); // 쿠키 만료
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
