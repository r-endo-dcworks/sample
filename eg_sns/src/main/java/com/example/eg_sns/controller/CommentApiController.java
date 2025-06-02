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

		ResponseComments saved = commentsService.saveComment(
				request.getPostsId(),
				request.getUsersId(),
				request.getComment());
		return ResponseEntity.ok(saved);
	}
}
