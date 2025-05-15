package com.example.eg_sns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.eg_sns.dto.RequestAccount;
import com.example.eg_sns.service.UsersService;

/**
 * ※TODO 適宜実装を入れてください。
 */
@Controller
@RequestMapping("/account")
public class AccountController {

	/**
	 * アカウント作成画面に遷移
	 * @return アカウント登録画面
	 */

	@Autowired
	private UsersService usersService;

	@GetMapping(path = { "", "/" })
	public String login() {
		return "account/index";
	}

	@PostMapping("/create")
	public String index(@ModelAttribute RequestAccount requestAccount) {
		usersService.save(requestAccount);
		return "redirect:/account/complete";
	}

	@GetMapping("/complete") 
	public String complete() {
		return "account/complete";
	}
}


