package com.aladin.common;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ImageStorageMamager {
	@Value("${file.upload.dir}")
	private String baseUploadDir; // 기본 저장 경로

	@Value("${file.default.profile-image}")
	public String defaultProfileImagePath; // 기본 프로필 이미지 경로

	@Value("${server.domain}")
	private String serverDomain; // 서버 도메인 (http://localhost:8080)

	public String saveImage(MultipartFile file, ImageType imageType) throws IOException {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("파일이 비어 있습니다.");
		}
		String fileName = generateUniqueFileName(file.getOriginalFilename());
		String folderPath = baseUploadDir + "/" + imageType.getFolder();

		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		File destinationFile = new File(folder, fileName);
		file.transferTo(destinationFile);

		log.info("파일 저장됨: {}", folderPath);
		return serverDomain + "/aladin/" + imageType.getFolder() + "/" + fileName;
	}

	private String generateUniqueFileName(String originalFilename) {
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		return System.currentTimeMillis() + "_" + Math.random() + extension;
	}
}
