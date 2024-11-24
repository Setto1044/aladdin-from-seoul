package com.aladin.openData.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.common.ApiResponseDto;
import com.aladin.openData.service.CommercialStatusService;
import com.aladin.openData.vo.CommercialStatusVO;

@RestController
@RequestMapping("/api/commercial-status")
public class CommercialStatusController {

	private final CommercialStatusService commercialStatusService;

	@Autowired
	public CommercialStatusController(CommercialStatusService commercialStatusService) {
		this.commercialStatusService = commercialStatusService;
	}

	// 특정 구 코드 조회
	@GetMapping("/{dongCode}")
	public ResponseEntity<ApiResponseDto<CommercialStatusVO>> getCommercialStatusByGuCode(@PathVariable String dongCode) {
		CommercialStatusVO status = commercialStatusService.getCommercialStatusByGuCode(dongCode);
		if (status == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(ApiResponseDto.of(true, "조회 성공.", status));
	}

	// 전체 조회
	@GetMapping
	public ResponseEntity<ApiResponseDto<List<CommercialStatusVO>>> getAllCommercialStatuses() {
		List<CommercialStatusVO> statuses = commercialStatusService.getAllCommercialStatuses();
		return ResponseEntity.ok(ApiResponseDto.of(true, "조회 성공.", statuses));
	}
}
