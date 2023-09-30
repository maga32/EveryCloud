package com.project.everycloud.controller.user;

import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppResponse;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/v1")
@RestController
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	@PostMapping("/getSessionUser")
	public AppResponse<UserDTO> getSessionUser() {
		UserDTO user = userService.getSessionUser((UserDTO) session.getAttribute("user"));

		return new AppResponse<UserDTO>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(user);
	}

	@PostMapping(value = "/updateUserForm")
	public AppResponse<UserDTO> updateUserForm(@RequestParam HashMap<String, Object> paramMap) {

		paramMap.put("user", (UserDTO) session.getAttribute("user"));
		UserDTO user = userService.updateUserForm(paramMap);

		return new AppResponse<UserDTO>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(user);
	}

	@PostMapping(value = "/checkOverlapId")
	public AppResponse<Boolean> checkOverlapId(@RequestParam String id) throws Exception {

		if(!StringUtils.hasText(id)) throw new Exception();
		Boolean result = (userService.getUser(id) == null);

		return new AppResponse<Boolean>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(result);
	}

	/*
	@PostMapping(value = "/checkOverlapId")
	Map<String, String> checkOverlapId(String id) {
		Map<String, String> map = new HashMap<String, String>();
		String result = (userService.getUser(id) == null) ? "ok" : "fail";
		map.put("result", result);

		return map;
	}
	*/

	/* ----------------- 수정필요 -----------------*/
	@GetMapping("/getUser")
	public Map<String, Object> getUser(@RequestParam("id") String id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("user", userService.getUser(id));
		
		return resultMap;
	}

	@RequestMapping("/login")
	String loginPage(Model model, @RequestParam(value="siteHtml", required=false, defaultValue="") String siteHtml) {
		UserDTO user = (UserDTO) session.getAttribute("user");
		if(userService.checkUserType(user) != 0) return "redirect:/";
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
			UserDTO loginUser = userService.getUser(id);
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


	@RequestMapping(value = "/updateUserProcess", method = RequestMethod.POST)
	String updateUserProcess(UserDTO user,
		@RequestParam(value = "userOrigId", required = false, defaultValue = "") String userOrigId,
		@RequestParam(value = "siteHtml", required = false, defaultValue = "/") String siteHtml) {

		UserDTO sessionUser = (UserDTO) session.getAttribute("user");

		if(userService.isAdmin(sessionUser) || (userService.isUser(sessionUser) && sessionUser.getId().equals(user.getId()))) {
			userService.updateUser(user, userOrigId);
			session.setAttribute("user", userService.getUser((userOrigId.equals("") ? user.getId() : userOrigId)));
		}

		return "redirect:" + siteHtml;
	}
}
