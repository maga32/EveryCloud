package com.everycloud.project.controller;

import javax.servlet.http.HttpSession;

import com.everycloud.project.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.everycloud.project.domain.User;

@Controller
public class MainController {
	
	@Autowired
	UserUtil userUtil;

	@RequestMapping("/")
	String mainPage(HttpSession session, Model model) {
		int userType = userUtil.checkUserType();

		if(userType == 0) {
			return "/user/login";
		} else if (userType == 2) {
			return "redirect:/file";
		} else if (userType == 1) {
			return "redirect:/guest";
		} else if (userType == 3) {
			return "redirect:/updateUser?type=admin";
		}
		
		return null;
	}

}
