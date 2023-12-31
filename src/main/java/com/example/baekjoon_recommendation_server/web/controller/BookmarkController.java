package com.example.baekjoon_recommendation_server.web.controller;

import java.awt.print.Book;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.baekjoon_recommendation_server.exception.ResponseMessage;
import com.example.baekjoon_recommendation_server.exception.StatusCode;
import com.example.baekjoon_recommendation_server.service.BookmarkService;
import com.example.baekjoon_recommendation_server.service.UserService;
import com.example.baekjoon_recommendation_server.web.dto.BookmarkDto;
import com.example.baekjoon_recommendation_server.web.dto.BookmarkRequestDto;
import com.example.baekjoon_recommendation_server.web.dto.MemoDto;
import com.example.baekjoon_recommendation_server.web.dto.UserRequestDto;
import com.example.baekjoon_recommendation_server.web.dto.UserResponseDto;
import com.example.baekjoon_recommendation_server.web.dto.base.DefaultRes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {
	private final BookmarkService bookmarkService;
	private final UserService userService;

	@GetMapping("/bookmarks")
	public ResponseEntity bookmarks(@RequestHeader("userId") String userId, @RequestHeader("password") String password){
		try{
			UserRequestDto.ValidateDto req = UserRequestDto.ValidateDto.builder()
				.userId(userId)
				.password(password)
				.build();
			UserResponseDto.ValidateResDto user = userService.getCurrentUser(req);
			List<BookmarkDto> bookmarkDtoList = bookmarkService.getBookmarks(user.getUserId());

			log.info("info : get bookmarks success");
			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.BOOKMARK_READ_SUCCESS, bookmarkDtoList), HttpStatus.OK);
		} catch (Exception e){
			log.error("error : {}", e);
			return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/{bookmarkId}")
	public ResponseEntity bookmarkDetail(@PathVariable Long bookmarkId){
		try{
			BookmarkDto bookmarkDto = bookmarkService.getBookmark(bookmarkId);

			log.info("info : get bookmark detail : {}", bookmarkId);
			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.BOOKMARK_READ_SUCCESS, bookmarkDto), HttpStatus.OK);
		} catch (Exception e){
			log.error("error : {}", e);
			return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/{problemId}")
	public ResponseEntity addBookmark(@PathVariable Long problemId, @RequestHeader("userId") String userId, @RequestHeader("password") String password,
		@RequestBody BookmarkRequestDto request){
		try{
			log.info("info : add bookmark : {}", problemId);

			UserRequestDto.ValidateDto req = UserRequestDto.ValidateDto.builder()
				.userId(userId)
				.password(password)
				.build();
			UserResponseDto.ValidateResDto user = userService.getCurrentUser(req);

			bookmarkService.addBookmark(user.getUserId(), request);

			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.BOOKMARK_ADD_SUCCESS), HttpStatus.OK);
		} catch (Exception e){
			log.error("error : {}", e);
			return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{bookmarkId}")
	public ResponseEntity deleteBookmark(@PathVariable Long bookmarkId, @RequestHeader("userId") String userId, @RequestHeader("password") String password){
		try{
			UserRequestDto.ValidateDto req = UserRequestDto.ValidateDto.builder()
				.userId(userId)
				.password(password)
				.build();
			UserResponseDto.ValidateResDto user = userService.getCurrentUser(req);

			bookmarkService.deleteBookmark(bookmarkId);

			log.info("info : delete bookmark success");
			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.BOOKMARK_DELETE_SUCCESS), HttpStatus.OK);
		} catch (Exception e){
			log.error("error : {}", e);
			return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{bookmarkId}")
	public ResponseEntity updateBookmarkMemo(@PathVariable Long bookmarkId, @RequestBody MemoDto request, @RequestHeader("userId") String userId, @RequestHeader("password") String password){
		try{
			log.info("info : update memo : {}", request.getMemo());

			UserRequestDto.ValidateDto req = UserRequestDto.ValidateDto.builder()
				.userId(userId)
				.password(password)
				.build();
			UserResponseDto.ValidateResDto user = userService.getCurrentUser(req);

			String memo = request.getMemo();
			bookmarkService.updateBookmark(bookmarkId, memo);
			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.BOOKMARK_UPDATE_SUCCESS), HttpStatus.OK);
		} catch (Exception e){
			log.error("error : {}", e);
			return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
		}
	}
}
