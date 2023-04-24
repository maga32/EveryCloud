package com.everycloud.project.controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.everycloud.project.domain.User;
import com.everycloud.project.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.everycloud.project.service.user.UserService;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	@Autowired
	UserUtil userUtil;
	
	@RequestMapping("/getUser")
	public Map<String, Object> getUser(@RequestParam("userId") String userId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("user", userService.getUser(userId));
		
		return resultMap;
	}

	@RequestMapping("/login")
	String loginPage(Model model, @RequestParam(value="siteHtml", required=false, defaultValue="") String siteHtml) {
		if(session.getAttribute("user") != null) return "redirect:/";
		model.addAttribute("siteHtml", siteHtml);
		return "/user/login";
	}

	@RequestMapping("/logout")
	String logout() {
		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping(value = "/loginProcess", method = RequestMethod.POST)
	String loginProcess(HttpServletResponse response, String userId, String userPass,
		@RequestParam(value="siteHtml", required=false, defaultValue="/") String siteHtml) throws IOException {
		String checkUser = userService.checkUser(userId, userPass);

		if (checkUser.equals("ok")) {
			User loginUser = userService.getUser(userId);
			session.setAttribute("user", loginUser);
			return "redirect:" + siteHtml;
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

	@RequestMapping(value = "/updateUser", method = RequestMethod.GET)
	String updateUser(Model model, String type,
		@RequestParam(value = "userId", required = false, defaultValue = "") String userId) {
		if(userId.equals("") && userUtil.isAdmin()) {
			model.addAttribute("user", userService.getAdmin());
		} else if(userUtil.isAdmin() || userId.equals(((User)session.getAttribute("user")).getUserId())) {
			model.addAttribute("user", userService.getUser(userId));
		}
		model.addAttribute("type",type);

		return "/user/updateUser";
	}

	@RequestMapping(value = "/checkOverlapId", method = RequestMethod.POST)
	@ResponseBody
	Map<String, String> checkOverlapId(String userId) {
		Map<String, String> map = new HashMap<String, String>();
		String result = (userService.getUser(userId) == null) ? "ok" : "fail";
		map.put("result", result);

		return map;
	}

	@RequestMapping(value = "/updateUserProcess", method = RequestMethod.POST)
	String updateUserProcess(User user,
		@RequestParam(value = "userOrigId", required = false, defaultValue = "") String userOrigId) {
		if(userUtil.isAdmin() || (userUtil.isUser() && ((User)session.getAttribute("user")).getUserId().equals(user.getUserId()))) {
			userService.updateUser(user, userOrigId);
		}

		return "redirect:/";
	}
}
