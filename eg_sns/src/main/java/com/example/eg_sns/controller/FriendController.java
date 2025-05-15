package com.example.eg_sns.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.eg_sns.entity.Friends;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.service.FriendsService;
import com.example.eg_sns.service.UsersService;

@Controller
@RequestMapping("/friend")
public class FriendController {

	@Autowired
	private UsersService usersService;
	@Autowired
	private FriendsService friendsService;

	@GetMapping(path = { "", "/" })
	public String list(HttpSession session, Model model) {

		Users loginUser = (Users) session.getAttribute("users");
		model.addAttribute("loginUser", loginUser);

		List<Users> allUsers = usersService.findAllUsers();
		Map<Long, Integer> friendStatusMap = new HashMap<>();
		Map<Long, String> friendDirectionMap = new HashMap<>();

		for (Users user : allUsers) {
			if (!user.getId().equals(loginUser.getId())) {
				Optional<Friends> friendOpt = friendsService.findFriendshipStatus(loginUser.getId(), user.getId());

				if (friendOpt.isPresent()) {
					Friends friend = friendOpt.get();
					friendStatusMap.put(user.getId(), friend.getFriendStatus());
					System.out.println("フレンド情報: ユーザーID = " + friend.getUsersId() +
							", フレンドID = " + friend.getFriendId() +
							", ステータス = " + friend.getFriendStatus());

					if (friend.getUsersId().equals(loginUser.getId())) {
						friendDirectionMap.put(user.getId(), "sent");
					} else {
						friendDirectionMap.put(user.getId(), "received");
					}
				} else {
					System.out.println("関係は見つかりませんでした。ユーザーID = " + user.getId());
				}
			}
		}

		model.addAttribute("usersList", allUsers);
		model.addAttribute("friendStatusMap", friendStatusMap);
		model.addAttribute("friendDirectionMap", friendDirectionMap);
		model.addAttribute("page", "friend");
		return "friend/list";
	}

	/**
	 * フレンド承認処理
	 * @return　profile画面
	 */
	@PostMapping("/approval")
	public String approvalFriend(@RequestParam("friendId") Long friendId, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("users");

		// フレンド承認のロジック
		System.out.println("フレンド承認を行います。");
		friendsService.approvalFriend(friendId, loginUser.getId());
		System.out.println("フレンド承認できました。");
		return "redirect:/friend";
	}

	/**
	 * フレンド却下処理
	 * @return　profile画面
	 */
	@PostMapping("/rejected")
	public String rejectedFriend(@RequestParam("friendId") Long friendId,
			HttpSession session) {
		Users loginUser = (Users) session.getAttribute("users");

		// フレンド却下のロジック
		System.out.println("フレンド却下を行います。");
		friendsService.rejectedFriend(friendId, loginUser.getId());

		return "redirect:/friend";
	}
	
	/**
	 * フレンド却下取り消し処理
	 * @return　profile画面
	 */
	@PostMapping("/cancel")
	public String cancelFriend(@RequestParam("friendId") Long friendId,
			HttpSession session) {
		Users loginUser = (Users) session.getAttribute("users");

		// フレンド却下のロジック
		System.out.println("フレンド解除を行います。");
		friendsService.deleteFriend(friendId, loginUser.getId());

		return "redirect:/friend";
	}
	/**
	 * フレンド解除処理
	 * @return　profile画面
	 */
	@PostMapping("/delete")
	public String deleteFriend(@RequestParam("friendId") Long friendId,
			HttpSession session) {
		Users loginUser = (Users) session.getAttribute("users");

		// フレンド却下のロジック
		System.out.println("フレンド解除を行います。");
		friendsService.deleteFriend(friendId, loginUser.getId());

		return "redirect:/friend";
	}
}