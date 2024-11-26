package com.aladin.util.chatGPT.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.aladin.util.chatGPT.model.ChatCompletionRequest;
import com.aladin.util.chatGPT.model.chatCompletionResponse;

@RestController
@RequestMapping("/gpt")
public class ChatGPTController {
	private final RestTemplate restTemplate;

	public ChatGPTController(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	@PostMapping
	public String getOpenaiResponse(@RequestBody String prompt) {
		ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest("gpt-3.5-mini", prompt);

		chatCompletionResponse response = restTemplate.postForObject("https://api.openai.com/v1/chat/completions", chatCompletionRequest, chatCompletionResponse.class);

		return response.getChoices().get(0).getMessage().getContent();
	}
}
