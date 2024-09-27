package com.project.everycloud.controller.user;

import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppList;
import com.project.everycloud.model.AppResponse;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RequestMapping("/api/v1/user")
@RestController
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	@PostMapping("/getSessionUser")
	public AppResponse<UserDTO> getSessionUser() {
		UserDTO user = userService.getSessionUser(sessionUser());

		return new AppResponse<UserDTO>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(user);
	}

	@PostMapping(value = "/updateUserForm")
	public AppResponse<UserDTO> updateUserForm(@RequestBody HashMap<String, Object> paramMap) {

		paramMap.put("user", sessionUser());
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

	@PostMapping("/createUser")
	public AppResponse<Boolean> createUser(@RequestBody HashMap<String, Object> paramMap) {

		userService.createUser(paramMap, sessionUser());

		return new AppResponse<Boolean>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(true);
	}

	@PostMapping("/updateUser")
	public AppResponse<Boolean> updateUser(@RequestBody HashMap<String, Object> paramMap) {

		setSessionUser(userService.updateUser(paramMap, sessionUser()));

		return new AppResponse<Boolean>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(true);
	}

	@PostMapping("/deleteUser")
	public AppResponse<Void> deleteUser(@RequestParam("userId") String userId) {

		userService.deleteUser(userId, sessionUser());

		return new AppResponse<Void>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message());
	}

	@PostMapping("/userList")
	public AppResponse<AppList<UserDTO>> getUserList(@RequestBody HashMap<String, Object> paramMap) {

		AppList<UserDTO> userList = userService.getUserList(paramMap, sessionUser());

		return new AppResponse<AppList<UserDTO>>()
				.setCode(ResponseType.SUCCESS.code())
				.setMessage(ResponseType.SUCCESS.message())
				.setData(userList);
	}

	@PostMapping("/login")
	public AppResponse<Boolean> login(@RequestBody HashMap<String, Object> paramMap) {

		UserDTO user = userService.login(paramMap);
		setSessionUser(user);

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

	private UserDTO sessionUser() {
		return (UserDTO) session.getAttribute("user");
	}

	private void setSessionUser(UserDTO user) {
		session.setAttribute("user", user);
	}
}
