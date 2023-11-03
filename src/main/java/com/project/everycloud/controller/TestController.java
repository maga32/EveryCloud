package com.project.everycloud.controller;

import com.project.everycloud.service.InstallService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@RequestMapping("/api/v1/test")
@RestController
public class TestController {

	@GetMapping("/setdb")
	public void testController() {
		InstallService installService = new InstallService();
		installService.dbInstall();
	}

	@GetMapping("/getdb")
	public void testController2(String table) {
		String path = System.getProperty("user.home") + File.separator + ".everyCloud" + File.separator + "EveryCloud.db";
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path+"?"+InstallService.decryptor(InstallService.getDbValue()));
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

}
