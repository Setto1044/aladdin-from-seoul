package com.aladin.util.chatGPT.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GptRestTemplateBean {

	@Value("${openai.key}")
	private String openAiKey;

	@Bean
	public RestTemplate restRestTemplate() {
		RestTemplate restRestTemplate = new RestTemplate();
		restRestTemplate.getInterceptors().add(((request, body, execution) -> {
			request.getHeaders().add("Authorization", "Bearer " + openAiKey);
			return execution.execute(request, body);
		}));
		return restRestTemplate;
	}
}
