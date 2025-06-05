package com.example.eg_sns.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eg_sns.dto.RequestAccount;
import com.example.eg_sns.dto.RequestModifyPassword;
import com.example.eg_sns.entity.Friends;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.service.FriendsService;
import com.example.eg_sns.service.LikeService;
import com.example.eg_sns.service.PostsService;
import com.example.eg_sns.service.UsersService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private UsersService usersService;
	@Autowired
	private PostsService postsService;
	@Autowired
	private FriendsService friendsService;
	@Autowired
	private LikeService likeService;

	
	
	@GetMapping("/{loginId}")
	public String index(@PathVariable("loginId") String loginId,
			HttpSession session, Model model) {
		Users loginUser = (Users) session.getAttribute("users");
		Users users = usersService.findUsers(loginId);

		model.addAttribute("loginUser", loginUser);
		model.addAttribute("users", users);
		session.setAttribute("userId", users.getId()); 

		Optional<Friends> friendOpt = friendsService.findFriendshipStatus(loginUser.getId(), users.getId());
		friendOpt.ifPresent(friend -> model.addAttribute("friendStatus", friend.getFriendStatus()));

		List<Posts> postsList = postsService.findPostsByUserId(users.getId());
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
		
		model.addAttribute("page", "profile");
		return "profile/index";
	}


	/**
	 * プロフィール更新処理
	 * @return　profile画面
	 */
	@PostMapping("/index")
	public String index(@ModelAttribute RequestAccount requestAccount, HttpSession session) {

		Users currentUser = (Users) session.getAttribute("users");
		if (requestAccount.getIconUri() == null || requestAccount.getIconUri().isEmpty()) {
			requestAccount.setIconUri(requestAccount.getProfileFileHidden());
		}
		usersService.update(requestAccount);

		currentUser.setName(requestAccount.getName());
		currentUser.setEmailAddress(requestAccount.getEmailAddress());
		currentUser.setProfile(requestAccount.getProfile());
		currentUser.setIconUri(requestAccount.getIconUri());
		session.setAttribute("users", currentUser);

		System.out.println("プロフィール更新完了: loginId=" + requestAccount.getLoginId() +
				", name=" + requestAccount.getName());
		return "redirect:/profile/" + currentUser.getLoginId();
	}

	/**
	 * パスワード更新処理
	 * @return　profile画面
	 */
	@PostMapping("/change-password")
	public String registPassword(
			@Validated @ModelAttribute RequestModifyPassword requestModifyPassword,
			BindingResult result,
			HttpSession session,
			RedirectAttributes redirectAttributes) {

		Users currentUser = (Users) session.getAttribute("users");
		Users users = (Users) session.getAttribute("users");

		String currentPassword = requestModifyPassword.getCurrentPassword();
		String newPassword = requestModifyPassword.getNewPassword();
		String renewPassword = requestModifyPassword.getRenewPassword();

		// 現在のパスワードチェック
		if (!users.getPassword().equals(currentPassword)) {
			result.rejectValue("currentPassword", "error.currentPassword", "現在のパスワードが正しくありません");
		}
		System.out.println("現在のパスワードチェック完了");

		// 新しいパスワードの一致チェック
		if (!newPassword.equals(renewPassword)) {
			result.rejectValue("renewPassword", "error.renewPassword", "新しいパスワードが一致しません");
		}
		System.out.println("新しいのパスワードチェック完了");

		// 入力エラーがあれば戻す
		if (result.hasErrors()) {
			return "profile/change-password";
		}

		users.setPassword(newPassword);
		usersService.updatePassword(users, newPassword);

		// セッション更新
		Users sessionUser = (Users) session.getAttribute("users");
		sessionUser.setPassword(newPassword);
		session.setAttribute("users", sessionUser);

		return "redirect:/profile/" + currentUser.getLoginId();
	}

	

	/**
	 * フレンド申請処理
	 * @return　profile画面
	 */
	@PostMapping("/apply")
	public String applyFriend(@RequestParam("friendId") Long friendId, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("users");

		friendsService.applyFriend(loginUser.getId(), friendId);
		Users friendUser = usersService.findById(friendId);
		return "redirect:/profile/" + friendUser.getLoginId();
	}

	/**
	 * フレンドブロック処理
	 * @return　profile画面
	 */
	@PostMapping("/rejected")
	public String blockFriend(@RequestParam("friendId") Long friendId,HttpSession session) {
		Users loginUser = (Users) session.getAttribute("users");
		friendsService.blockFriend(friendId, loginUser.getId());
		Users friendUser = usersService.findById(friendId);
		return "redirect:/profile/" + friendUser.getLoginId();
	}
	
	/**
	 * トピック削除アクション。
	 * @param topicsId トピックID
	 */
	@PostMapping("/delete")
	public String deletePosts(@RequestParam Long postsId, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("users");
		Long usersId = loginUser.getId();
		postsService.deletePosts(postsId, usersId);
		Users user = (Users) session.getAttribute("users");
		return "redirect:/profile/" + user.getLoginId();
	}
}
