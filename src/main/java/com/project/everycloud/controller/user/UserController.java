package com.project.everycloud.controller.user;

import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppResponse;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

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
		Boolean result = (userService.getUserInfo(id) == null);

		return new AppResponse<Boolean>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(result);
	}

	@PostMapping("/updateUser")
	public AppResponse<Boolean> updateUser(@RequestBody HashMap<String, Object> paramMap) {

		paramMap.put("sessionUser", (UserDTO) session.getAttribute("user"));
		session.setAttribute("user", userService.updateUser(paramMap));

		return new AppResponse<Boolean>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(true);
	}

	@RequestMapping("/login")
	public AppResponse<Boolean> login(@RequestBody HashMap<String, Object> paramMap) {

		UserDTO user = userService.login(paramMap);
		session.setAttribute("user", user);

		return new AppResponse<Boolean>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(true);
	}

	@PostMapping("/logout")
	public AppResponse<Boolean> logout() {

		session.invalidate();

		return new AppResponse<Boolean>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(true);
	}

}
