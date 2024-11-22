package com.aladin.roomBoard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aladin.common.ImageStorageMamager;
import com.aladin.common.ImageType;
import com.aladin.exceptions.BoardCreationException;
import com.aladin.exceptions.ResourceNotFoundException;
import com.aladin.roomBoard.dto.RoomBoardCardDto;
import com.aladin.roomBoard.dto.RoomBoardDetailDto;
import com.aladin.roomBoard.dto.RoomBoardInsertRequestDto;
import com.aladin.roomBoard.dto.RoomBoardUpdateRequestDto;
import com.aladin.roomBoard.mapper.RoomBoardMapper;
import com.aladin.roomBoard.vo.RoomBoardImageVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class RoomBoardServiceImpl implements RoomBoardService {

	private final RoomBoardMapper boardMapper;
	private final ImageStorageMamager imageStorageMamager;

	public RoomBoardServiceImpl(RoomBoardMapper boardMapper, ImageStorageMamager imageStorageMamager) {
		this.boardMapper = boardMapper;
		this.imageStorageMamager = imageStorageMamager;
	}

	@Override
	public Long createBoard(RoomBoardInsertRequestDto requestDto) {
		try {
			boardMapper.insertBoard(requestDto);

			Long boardId = requestDto.getId();

			saveImages(requestDto.getImages(), boardId);

			return boardId;
		} catch (Exception e) {
			log.error("게시글 생성 중 오류: {}", e.getMessage(), e);
			throw new BoardCreationException("게시글 등록 중 오류가 발생했습니다.", e);
		}
	}

	@Override
	public List<RoomBoardCardDto> findBoardsByCursor(Long cursorId, Long pageSize) {
		List<RoomBoardCardDto> boards = boardMapper.findBoardsByCursor(cursorId, pageSize);
		if (boards == null || boards.isEmpty()) {
			throw new ResourceNotFoundException("게시물을 찾을 수 없습니다.");
		}
		return boards;
	}

	@Override
	public RoomBoardDetailDto getBoardDetail(Long roomboardsId) {
		try {
			RoomBoardDetailDto boardDetailDto = boardMapper.findOneByRoomBoardId(roomboardsId);

			if (boardDetailDto == null || boardDetailDto.getRoomCardInfo() == null) {
				throw new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다.");
			}

			// 이미지 링크
			List<RoomBoardImageVo> roomImages = boardMapper.findImagesByRoomBoardId(roomboardsId);
			boardDetailDto.setRoomImageInfos(roomImages);

			return boardDetailDto;
		} catch (Exception e) {
			log.error("게시물 상세 조회 중 오류: {}", e.getMessage(), e);
			throw new ResourceNotFoundException("게시물 조회 중 오류가 발생했습니다.", e);
		}
	}

	@Override
	public Long updateBoard(RoomBoardUpdateRequestDto requestDto) {
		try {
			boardMapper.updateBoard(requestDto);

			Long boardId = requestDto.getId();

			boardMapper.deleteImagesByRoomBoardId(boardId);

			saveImages(requestDto.getImages(), boardId);

			return boardId;
		} catch (Exception e) {
			log.error("게시글 수정 중 오류: {}", e.getMessage(), e);
			throw new BoardCreationException("게시글 수정 중 오류가 발생했습니다.", e);
		}
	}

	/**
	 * 이미지 저장 로직 공통화
	 * 
	 * @param images  저장할 이미지 목록
	 * @param boardId 게시글 ID
	 */
	private void saveImages(List<MultipartFile> images, Long boardId) {
		if (images != null && !images.isEmpty()) {
			int imageOrder = 0;
			for (MultipartFile image : images) {
				try {
					String imageUrl = imageStorageMamager.saveImage(image, ImageType.POST);
					boardMapper.insertBoardImage(boardId, imageUrl, imageOrder++);
				} catch (Exception e) {
					log.error("이미지 저장 실패: {}", e.getMessage(), e);
					throw new BoardCreationException("이미지 저장 중 오류가 발생했습니다.", e);
				}
			}
		}
	}

	@Override
	public int deleteBoard(Long boardId) {
		try {
			int deletedCount = boardMapper.deleteBoardById(boardId);

			if (deletedCount == 0) {
				log.warn("삭제 대상 게시글이 존재하지 않습니다. 게시글 ID: {}", boardId);
				throw new BoardCreationException("게시글 삭제 중 오류가 발생했습니다.");
			}
			return deletedCount;
		} catch (Exception e) {
			log.error("게시글 삭제 중 오류 발생: {}", e.getMessage(), e);
			throw new BoardCreationException("게시글 삭제 중 오류가 발생했습니다.", e);
		}
	}

}
