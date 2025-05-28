package com.example.eg_sns.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eg_sns.service.LikeService;

@RestController
@RequestMapping("/api")
public class LikeApiController {

	@Autowired
	private LikeService likeService;

	/**
	 * いいねのトグルを行うAPI
	 * @param postsId 投稿ID
	 * @param userDetails ログイン中のユーザー情報（Spring Security）
	 * @return いいね数と、現在のいいね状態
	 */
	@PostMapping("/like/{postsId}")
	public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long postsId,
			HttpSession session) {
		Long usersId = (Long) session.getAttribute("userId"); 
		Map<String, Object> result = likeService.toggleLike(postsId, usersId); 
		return ResponseEntity.ok(result); 
	}
}
