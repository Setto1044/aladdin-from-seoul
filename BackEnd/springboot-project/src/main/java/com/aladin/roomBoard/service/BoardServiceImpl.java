package com.aladin.roomBoard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aladin.common.ImageStorageMamager;
import com.aladin.common.ImageType;
import com.aladin.exceptions.BoardCreationException;
import com.aladin.roomBoard.dto.BoardCardDto;
import com.aladin.roomBoard.dto.BoardDetailDto;
import com.aladin.roomBoard.dto.BoardInsertRequestDto;
import com.aladin.roomBoard.dto.BoardUpdateRequestDto;
import com.aladin.roomBoard.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class BoardServiceImpl implements BoardService {

	private final BoardMapper boardMapper;
	private final ImageStorageMamager imageStorageMamager;

	public BoardServiceImpl(BoardMapper boardMapper, ImageStorageMamager imageStorageMamager) {
		this.boardMapper = boardMapper;
		this.imageStorageMamager = imageStorageMamager;
	}

	@Override
	public Long createBoard(BoardInsertRequestDto requestDto) {
		try {
			boardMapper.insertBoard(requestDto);

			// 생성된 ID 반환
			Long boardId = requestDto.getId(); // DTO에서 자동 생성된 ID 반환

			// 사진 추가
			int imageOrder = 0;
			if (requestDto.getImages() != null && !requestDto.getImages().isEmpty()) {
				try {
					for (MultipartFile image : requestDto.getImages()) {
						// 이미지 저장 후 URL 생성
						String imageUrl = imageStorageMamager.saveImage(image, ImageType.POST);
						// DB에 이미지 경로 저장
						boardMapper.insertBoardImage(boardId, imageUrl, imageOrder++);
					}
				} catch (Exception e) {
					throw new RuntimeException("이미지 저장 실패", e);
				}

			}

			return boardId;
		} catch (Exception e) {
			log.error(e.toString());
			throw new BoardCreationException("게시글 등록 중 오류가 발생했습니다.", e);
		}

	}

	@Override
	public List<BoardCardDto> findBoardsByCursor(Long cursorId, Long pageSize) {
		return boardMapper.findBoardsByCursor(cursorId, pageSize);
	}

	@Override
	public BoardDetailDto getBoardDetail(Long roomboardsId) {
		BoardDetailDto boardDetailDto = new BoardDetailDto();
		boardDetailDto.setRoomCardInfo(boardMapper.findOneByRoomBoardId(roomboardsId));
		boardDetailDto.setRoomImageInfos(boardMapper.findImagesByRoomBoardId(roomboardsId));
		return boardDetailDto;
	}

	@Override
	@Transactional
	public Long updateBoard(BoardUpdateRequestDto requestDto) {
		try {
			// 게시물 기본 정보 수정
			boardMapper.updateBoard(requestDto);

			Long boardId = requestDto.getId();

			// 기존 이미지 삭제
			boardMapper.deleteImagesByRoomBoardId(boardId);

			// 새 이미지 저장
			int imageOrder = 0;
			if (requestDto.getImages() != null && !requestDto.getImages().isEmpty()) {
				for (MultipartFile image : requestDto.getImages()) {
					String imageUrl = imageStorageMamager.saveImage(image, ImageType.POST);
					boardMapper.insertBoardImage(boardId, imageUrl, imageOrder++);
				}
			}
			return boardId;
		} catch (Exception e) {
			log.error(e.toString());
			throw new BoardCreationException("게시글 수정 중 오류가 발생했습니다.", e);
		}
	}

}
