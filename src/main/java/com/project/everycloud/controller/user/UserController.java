package com.project.everycloud.controller.user;

import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppList;
import com.project.everycloud.model.AppResponse;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.service.UserService;
import com.project.everycloud.common.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	@Autowired
	UserUtil userUtil;

	@RequestMapping("/getSessionUser")
	public AppResponse<UserDTO> getSessionUser() {
		UserDTO user = (UserDTO) session.getAttribute("user");
		UserDTO admin = userService.getAdmin();
		if(userUtil.checkUserType(user, admin) == 3) {
			throw new
		}

		return new AppResponse<UserDTO>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(user);
	}

	@PostMapping("/getUser")
	public Map<String, Object> getUser(@RequestParam("id") String id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("user", userService.getUser(id));
		
		return resultMap;
	}

	@RequestMapping("/login")
	String loginPage(Model model, @RequestParam(value="siteHtml", required=false, defaultValue="") String siteHtml) {
		UserDTO user = (UserDTO) session.getAttribute("user");
		UserDTO admin = userService.getAdmin();
		if(userUtil.checkUserType(user, admin) != 0) return "redirect:/";
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

	@RequestMapping(value = "/updateUser")
	String updateUser(Model model, HttpServletRequest request, String type,
		@RequestParam(value = "id", required = false, defaultValue = "") String id,
		@RequestParam(value = "siteHtml", required = false, defaultValue = "/") String siteHtml) {

		UserDTO user = (UserDTO) session.getAttribute("user");
		UserDTO admin = userService.getAdmin();

		if(userUtil.checkUserType(user, admin) == 0) return "redirect:/";
		if(id.equals("") && userUtil.isAdmin(session)) {
			model.addAttribute("user", userService.getAdmin());
		} else if(userUtil.isAdmin(session) || id.equals(((UserDTO)session.getAttribute("user")).getId())) {
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
	String updateUserProcess(UserDTO user,
		@RequestParam(value = "userOrigId", required = false, defaultValue = "") String userOrigId,
		@RequestParam(value = "siteHtml", required = false, defaultValue = "/") String siteHtml) {
		if(userUtil.isAdmin(session) || (userUtil.isUser(session) && ((UserDTO)session.getAttribute("user")).getId().equals(user.getId()))) {
			userService.updateUser(user, userOrigId);
			session.setAttribute("user", userService.getUser((userOrigId.equals("") ? user.getId() : userOrigId)));
		}

		return "redirect:" + siteHtml;
	}
}
