package com.example.eg_sns.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eg_sns.dto.PostDto;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.service.LikeService;
import com.example.eg_sns.service.PostsService;

@RestController
@RequestMapping("/api")
public class PostApiController {

	@Autowired
	private PostsService postsService;
	@Autowired
	private LikeService likeService;
	
	@GetMapping("/getPosts")
	public Map<String, Object> getPosts(@RequestParam(name = "sinceId", required = false) Long sinceId,
			HttpSession session, 
			Model model) {
		
		//ログインユーザー情報の取得
		Users loginUser = (Users) session.getAttribute("users");
		model.addAttribute("loginUser", loginUser);
		
		//投稿情報の取得
		List<Posts> posts;		
		if (sinceId == null){
			posts = postsService.findLatestPosts();
		} else {
			posts = postsService.findNextPosts(sinceId);
		}
		
		//PostDtoに投稿情報、いいねの有無、投稿のいいねの数を格納
		 List<PostDto> postDtos = posts.stream().map(post -> {
		        Long postId = post.getId();
		        int likeCount = likeService.countLikesByPostId(post.getId());
		        boolean liked = likeService.isLikedByUser(postId, loginUser.getId());
		        return new PostDto(post, likeCount, liked);
		    }).collect(Collectors.toList());
		
		// 次ページの有無と次の sinceId を設定
		boolean hasNext = posts.size() == 5; // 取得した投稿が５件未満の場合falseを返す
		Long nextSinceId = hasNext ? posts.get(posts.size() - 1).getId() : null;
		
		Map<String, Object> result = new HashMap<>();
		result.put("data", postDtos);
		
		Map<String, Object> pageInfo = new HashMap<>();
		pageInfo.put("has_next", hasNext);
		pageInfo.put("since_id", nextSinceId);
		
		result.put("page_info", pageInfo);
		return result;
	}
}
