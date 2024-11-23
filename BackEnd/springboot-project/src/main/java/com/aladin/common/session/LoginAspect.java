package com.aladin.common.session;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoginAspect {

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private HttpServletRequest request;

	@Before("@annotation(com.aladin.common.annotation.LoginCheck)")
	public void checkLogin() throws Exception {
		String username = sessionManager.getSessionUser(request);

		if (username == null) {
			throw new IllegalStateException("로그인이 필요합니다."); // 커스텀 예외로 변경 가능
		}
	}
}