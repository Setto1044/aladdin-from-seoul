package com.aladin.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${file.upload.dir}")
	private String uploadDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 게시글 이미지 경로 매핑
		registry.addResourceHandler("/post/**").addResourceLocations("file:///" + uploadDir + "/post/");

		// 프로필 이미지 경로 매핑
		registry.addResourceHandler("/profile/**").addResourceLocations("file:///" + uploadDir + "/profile/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 모든 경로에 대해 적용
				.allowedOrigins("*") // 모든 도메인 허용
				.allowedMethods("*") // 모든 HTTP 메서드 허용
				.allowedHeaders("*") // 모든 헤더 허용
				.allowCredentials(false); // 인증 정보를 포함하지 않음
	}
}