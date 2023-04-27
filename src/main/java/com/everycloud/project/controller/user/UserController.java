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

import javax.servlet.http.HttpServletRequest;
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
	public Map<String, Object> getUser(@RequestParam("id") String id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("user", userService.getUser(id));
		
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
	String loginProcess(HttpServletResponse response, String id, String pass,
		@RequestParam(value="siteHtml", required=false, defaultValue="/") String siteHtml) throws IOException {
		String checkUser = userService.checkUser(id, pass);

		if (checkUser.equals("ok")) {
			User loginUser = userService.getUser(id);
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

	@RequestMapping(value = "/updateUser")
	String updateUser(Model model, HttpServletRequest request, String type,
		@RequestParam(value = "id", required = false, defaultValue = "") String id,
		@RequestParam(value = "siteHtml", required = false, defaultValue = "/") String siteHtml) {

		if(userUtil.checkUserType() == 0) return "redirect:/";
		if(id.equals("") && userUtil.isAdmin()) {
			model.addAttribute("user", userService.getAdmin());
		} else if(userUtil.isAdmin() || id.equals(((User)session.getAttribute("user")).getId())) {
			model.addAttribute("user", userService.getUser(id));
		}

		model.addAttribute("type",type);
		model.addAttribute("siteHtml",siteHtml);

		return "/user/updateUser";
	}

	@RequestMapping(value = "/checkOverlapId", method = RequestMethod.POST)
	@ResponseBody
	Map<String, String> checkOverlapId(String id) {
		Map<String, String> map = new HashMap<String, String>();
		String result = (userService.getUser(id) == null) ? "ok" : "fail";
		map.put("result", result);

		return map;
	}

	@RequestMapping(value = "/updateUserProcess", method = RequestMethod.POST)
	String updateUserProcess(User user,
		@RequestParam(value = "userOrigId", required = false, defaultValue = "") String userOrigId,
		@RequestParam(value = "siteHtml", required = false, defaultValue = "/") String siteHtml) {
		if(userUtil.isAdmin() || (userUtil.isUser() && ((User)session.getAttribute("user")).getId().equals(user.getId()))) {
			userService.updateUser(user, userOrigId);
			session.setAttribute("user", userService.getUser((userOrigId.equals("") ? user.getId() : userOrigId)));
		}

		return "redirect:" + siteHtml;
	}
}
