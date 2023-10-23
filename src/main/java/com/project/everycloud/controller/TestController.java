package com.project.everycloud.controller;

import com.project.everycloud.service.InstallService;
import com.project.everycloud.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@RequestMapping("/api/v1/test")
@RestController
public class TestController {
	@Autowired
	SettingsService settingsService;

	@GetMapping("/setdb")
	public void testController() {
		InstallService installService = new InstallService();
		installService.dbInstall();
	}

	@GetMapping("/getdb")
	public void testController2(String table) {
		String path = System.getProperty("user.home") + File.separator + ".everyCloud" + File.separator + "EveryCloud.db";
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path+"?cipher=sqlcipher&legacy=4&key=louise");
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + table + ";");

			System.out.println("!!!!!" + table + " table list : ");
			if(table.equals("user")) {
				while (rs.next()) {
					String id = rs.getString("id");
					String pass = rs.getString("pass");
					String nickname = rs.getString("nickname");
					String email = rs.getString("email");
					String auth = rs.getString("auth");
					String group_no = rs.getString("group_no");

					System.out.println("id = '" + id + "', pass = '" + pass + "', nickname = '" + nickname + "', email = '" + email + "', auth = '" + auth + "', group_no = " + group_no);
				}
			} else if(table.equals("settings")) {
				while (rs.next()) {
					String type = rs.getString("type");
					String external_url = rs.getString("external_url");

					System.out.println("type = '" + type + "', external_url = '" + external_url +"'");
				}
			} else if(table.equals("group_setting")) {
				while (rs.next()) {
					String no = rs.getString("no");
					String name = rs.getString("name");

					System.out.println("no = " + no + ", name = '" + name +"'");
				}
			} else {
				System.out.println(table + "table is not exist.");
			}

			connection.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		System.out.println("finished");
	}

	@GetMapping("/getMac")
	public void getMac() {
		System.out.println(InstallService.getMac());
	}

	@GetMapping("/toHex")
	public void toHex(String str) {
		str += "francoise";
		System.out.println("입력 : " + str);

		byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
		StringBuilder result = new StringBuilder();
		for (byte b : bytes) {
			result.append(String.format("%02X", b));
		}
		String hexStr = result.toString();
		System.out.println("HEX : " + hexStr);
	}

	@GetMapping("/fromHex")
	public void fromHex(String str) {
		System.out.println("HEX : " + str);

		int len = str.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4)
					+ Character.digit(str.charAt(i+1), 16));
		}

		String org = new String(data, StandardCharsets.UTF_8);
		System.out.println("출력 : " + org.replace("francoise",""));
	}
}
