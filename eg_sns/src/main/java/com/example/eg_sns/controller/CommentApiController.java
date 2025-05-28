package com.example.eg_sns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eg_sns.dto.RequestComments;
import com.example.eg_sns.dto.ResponseComments;
import com.example.eg_sns.service.CommentsService;

@RestController
@RequestMapping("/api")
public class CommentApiController {

	@Autowired
	private CommentsService commentsService;

	@PostMapping("/comments")
	public ResponseEntity<ResponseComments> create(@RequestBody RequestComments request) {

		System.out.println("commentAPIに入りました。");
		System.out.println("リクエストから取得した getPostsId = " + request.getPostsId());
		System.out.println("リクエストから取得した getUsersId = " + request.getUsersId());
		System.out.println("リクエストから取得した getComment = " + request.getComment());

		ResponseComments saved = commentsService.saveComment(
				request.getPostsId(),
				request.getUsersId(),
				request.getComment());
		System.out.println("saveComment処理が完了しました");
		return ResponseEntity.ok(saved);
	}
}
