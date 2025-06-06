package com.example.eg_sns.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.eg_sns.dto.RequestAccount;
import com.example.eg_sns.dto.RequestPosts;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.service.LikeService;
import com.example.eg_sns.service.PostsService;
import com.example.eg_sns.service.UsersService;

@Controller
@RequestMapping("/home")
public class HomeController {

	@Autowired
	private UsersService usersService;
	@Autowired
	private PostsService postsService;
	@Autowired
	private LikeService likeService;

	@GetMapping(path = { "", "/" })
	public String index(HttpSession session, Model model,
			@RequestParam(required = false) String imageUri,
			String comment) {
		Users loginUser = (Users) session.getAttribute("users");
		model.addAttribute("loginUser", loginUser);
		Users users = (Users) session.getAttribute("users");
		model.addAttribute("users", users);
		session.setAttribute("userId", users.getId()); 

		List<Posts> postsList = postsService.findLatestPosts();//投稿５件取得
		model.addAttribute("posts", postsList);
		  Map<Long, Integer> likeCountMap = new HashMap<>();
		    Map<Long, Boolean> likedMap = new HashMap<>();

		    for (Posts post : postsList) {
		        Long postId = post.getId();
		        int likeCount = likeService.countLikesByPostId(postId);
		        likeCountMap.put(postId, likeCount);

		        if (loginUser != null) {
		            boolean liked = likeService.isLikedByUser(postId, loginUser.getId());
		            likedMap.put(postId, liked);
		        } else {
		            likedMap.put(postId, false);
		        }
		    }

		    model.addAttribute("likeCountMap", likeCountMap);
		    model.addAttribute("likedMap", likedMap);
		Long sinceId = postsList.isEmpty() ? 0L : postsList.get(postsList.size() - 1).getId();
		model.addAttribute("sinceId", sinceId);

		model.addAttribute("page", "home");
		return "home/index";
	}

	/**
	 * ログイン処理後HOME画面に遷移
	 * home画面に遷移
	 * @return　home画面
	 */
	@PostMapping(path = { "", "/" })
	public String index(@ModelAttribute RequestAccount requestAccount, Model model, HttpSession session) {
		String loginId = requestAccount.getLoginId();
		String password = requestAccount.getPassword();

		Users users = usersService.findUsers(loginId, password);

		if (users == null) {
			model.addAttribute("users", users);
			return "error/505";
		}

		// セッションに保存
		System.out.println("ログイン成功、ユーザーをセッションに保存: " + users.getLoginId());
		session.setAttribute("users", users);
		return "redirect:home";
	}

	/**
	 * 投稿処理
	 * @return　home画面
	 */
	@PostMapping("/share")
	public String index(@ModelAttribute RequestPosts requestPosts, HttpSession session,
			@RequestParam(required = false) String imageUri) {
		Users user = (Users) session.getAttribute("users");
		String postImagesUri = null;
		System.out.println("ImageUri = " + imageUri);
		if (imageUri != null && !imageUri.isEmpty()) {
			postImagesUri = imageUri;
		}
		postsService.save(requestPosts, user.getId(), postImagesUri);
		return "redirect:/home";
	}

}
