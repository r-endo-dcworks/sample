package com.example.eg_sns.controller;

import java.util.List;

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
import com.example.eg_sns.entity.PostComments;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.service.CommentsService;
import com.example.eg_sns.service.PostsService;
import com.example.eg_sns.service.UsersService;

/**
 * ※TODO 適宜実装を入れてください。
 */

@Controller
@RequestMapping("/home")
public class HomeController {

	@Autowired
	private UsersService usersService;
	@Autowired
	private PostsService postsService;  
	@Autowired
	private CommentsService commentsService;  

	@GetMapping(path = { "", "/" })
	public String index(HttpSession session, Model model, 
			@RequestParam(required = false) String imageUri,
			String comment) {
		Users loginUser = (Users) session.getAttribute("users");
		model.addAttribute("loginUser", loginUser);
		System.out.println("GET /home にアクセスされました");
		Users users = (Users) session.getAttribute("users");
		System.out.println("ユーザー情報: ID=" + users.getLoginId() +
				", メール=" + users.getEmailAddress() +
				", 名前=" + users.getName());

		model.addAttribute("users", users);
			List<Posts> postsList = postsService.findLatestPosts();//投稿５件取得
		
		for (Posts post : postsList) {
			System.out.println("投稿タイトル: " + post.getTitle());

			if (post.getPostCommentsList() != null && !post.getPostCommentsList().isEmpty()) {
				for (PostComments pc : post.getPostCommentsList()) {
					System.out.println(" → コメント: " + pc.getComment() + " / 投稿者ID: " + pc.getUsersId());
				}
			} else {
				System.out.println(" → コメントなし");
			}
		}
	
		model.addAttribute("posts", postsList);
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
		System.out.println("投稿処理を開始します。");
		Users user = (Users) session.getAttribute("users");
		String postImagesUri = null;
		System.out.println("ImageUri = " + imageUri);
		if (imageUri != null && !imageUri.isEmpty()) {
			postImagesUri = imageUri;
		}
		postsService.save(requestPosts, user.getId(), postImagesUri);
		System.out.println("投稿が保存されました。");
		return "redirect:/home";
	}
	
	/**
	 * コメント投稿処理
	 * @return　home画面
	 */
	@PostMapping("/comment")
	public String index(@RequestParam Long postId,
            @RequestParam String comment,
            HttpSession session) {
		System.out.println("コメント処理を開始します。");
		System.out.println("comment = " + comment);
		Users user = (Users) session.getAttribute("users");
		commentsService.saveComment(postId, user.getId(), comment);	
	

	System.out.println("コメントが保存されました。");
		return "redirect:/home";
	}

}
