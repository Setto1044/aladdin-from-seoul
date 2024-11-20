package com.aladin.common;

public enum ImageType {
	PROFILE("profile"), // 프로필 이미지
	POST("post"); // 게시물 이미지

	private final String folder;

	ImageType(String folder) {
		this.folder = folder;
	}

	public String getFolder() {
		return folder;
	}
}