package com.aladin.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.aladin.exceptions.BoardCreationException;
import com.aladin.exceptions.MemberNotFoundException;
import com.aladin.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BoardCreationException.class)
	public ResponseEntity<ApiResponseDto> handleBoardCreationException(BoardCreationException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.of(false, "등록에 실패했습니다.", e.getMessage()));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseDto> handleResourceNotFoundException(ResourceNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.of(false, "리소스를 찾을 수 없습니다.", e.getMessage()));
	}

	@ExceptionHandler(MemberNotFoundException.class)
	public ResponseEntity<ApiResponseDto> handleMemberNotFoundException(MemberNotFoundException e) {
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of(false, "회원 정보를 찾을 수 없습니다.", e.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponseDto> handleGlobalException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.of(false, "서버 오류가 발생했습니다.", e.getMessage()));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponseDto> handleRuntimeException(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.of(false, e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponseDto> handleValidationException(MethodArgumentNotValidException e) {
		String message = "잘못된 값이 전달되었습니다: " + e.getBindingResult().getFieldError().getRejectedValue();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.of(false, message));
	}

}
