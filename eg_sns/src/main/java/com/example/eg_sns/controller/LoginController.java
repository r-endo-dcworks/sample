package com.example.eg_sns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/login")
public class LoginController {

	@GetMapping("/index")  

	public String index() {
		return "login/index";
		
	}
}
