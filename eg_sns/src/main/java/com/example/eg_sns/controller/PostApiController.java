package com.example.eg_sns.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.service.PostsService;

@RestController
@RequestMapping("/api")
public class PostApiController {

	@Autowired
	private PostsService postsService;

	@GetMapping("/getPosts")
	public Map<String, Object> getPosts(@RequestParam(name = "sinceId", required = false) Long sinceId) {


		List<Posts> posts;		
		if (sinceId == null){
			posts = postsService.findLatestPosts();
		} else {
			posts = postsService.findNextPosts(sinceId);
		}
		
		
		// 次ページの有無と次の sinceId を設定
		boolean hasNext = posts.size() == 5; // 取得した投稿が５件未満の場合falseを返す
		Long nextSinceId = hasNext ? posts.get(posts.size() - 1).getId() : null;

		
		Map<String, Object> result = new HashMap<>();
		result.put("data", posts);

		Map<String, Object> pageInfo = new HashMap<>();
		pageInfo.put("has_next", hasNext);
		pageInfo.put("since_id", nextSinceId);
		result.put("page_info", pageInfo);

		return result;
	}
}
