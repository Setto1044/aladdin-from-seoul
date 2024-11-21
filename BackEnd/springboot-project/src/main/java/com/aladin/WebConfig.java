package com.aladin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
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
}