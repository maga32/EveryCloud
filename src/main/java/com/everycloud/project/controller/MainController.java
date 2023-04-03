package com.everycloud.project.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.everycloud.project.domain.User;
import com.everycloud.project.service.user.UserService;

@Controller
public class MainController {
	
	@Autowired
	UserService userService;

	@RequestMapping("/")
	String mainPage(HttpSession session) {
		User user = (User) session.getAttribute("user");
		
		if(user == null ) {
			return "/login";
		} else if (user.getUserAuthority().equals("Y")) {
			return "redirect:/file";
		} else if (user.getUserAuthority().equals("N")) {
			return "redirect:/guest";
		}
		
		return null;
	}
	
	@RequestMapping(value = "/loginProcess", method = RequestMethod.POST)
	String loginProcess(HttpSession session, HttpServletResponse response, String userId, String userPass) throws IOException {
		String checkUser = userService.checkUser(userId, userPass);
		
		if(checkUser.equals("ok")) {
			User loginUser = userService.getUser(userId);
			session.setAttribute("user", loginUser);
			
			return "redirect:/";
		} else {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("	alert('" + checkUser + "');");
			out.println("	history.back();");
			out.println("</script>");
			
			return null;
		}
		
	}
	
}
