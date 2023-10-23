package com.project.everycloud.controller;

import com.project.everycloud.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.sql.*;

@RequestMapping("/api/v1/test")
@RestController
public class TestController {
	@Autowired
	SettingsService settingsService;

	@GetMapping("/setdb")
	public void testController() {
		String path = System.getProperty("user.home") + File.separator + ".everyCloud" + File.separator + "EveryCloud2.db";
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path+"?cipher=sqlcipher&legacy=4&key=test");

			Statement stmt = connection.createStatement();
			stmt.execute("CREATE TABLE 'settings' ('type' TEXT, 'external_url' TEXT, PRIMARY KEY('type'))");
			stmt.execute("CREATE TABLE 'group_setting' ('no' INTEGER, 'name' TEXT NOT NULL, PRIMARY KEY('no'))");
			stmt.execute("CREATE TABLE 'share' ('link'TEXT, 'path'TEXT NOT NULL UNIQUE, 'date' TEXT, 'method' INTEGER NOT NULL, 'pass' TEXT, 'auth' INTEGER NOT NULL DEFAULT 0, PRIMARY KEY('link'))");
			stmt.execute("CREATE TABLE 'share_group' ('share_link' TEXT NOT NULL, 'group_no' INTEGER NOT NULL, 'auth' INTEGER NOT NULL DEFAULT 0, PRIMARY KEY('share_link', 'group_no'), FOREIGN KEY('group_no') REFERENCES 'group_setting'('no'), FOREIGN KEY('share_link') REFERENCES 'share'('link'))");
			stmt.execute("CREATE TABLE 'user' ('id' TEXT, 'pass' TEXT NOT NULL, 'nickname' TEXT NOT NULL, 'email' TEXT, 'auth' TEXT NOT NULL DEFAULT 'N', 'group_no' INTEGER NOT NULL DEFAULT 1, FOREIGN KEY('group_no') REFERENCES 'group_setting'('no'), PRIMARY KEY('id'))");

			stmt.execute("INSERT INTO 'settings' VALUES('default', 'http://127.0.0.1:11024')");
			stmt.execute("INSERT INTO 'settings' VALUES('admin', 'http://127.0.0.1:11024')");
			stmt.execute("INSERT INTO 'group_setting' VALUES(1, 'default')");
			stmt.execute("INSERT INTO 'user' VALUES('admin', 'admin', 'admin', null, 'Y', 1)");

			connection.close();
		} catch (final SQLException e) {
			e.printStackTrace();
		}

		System.out.println("finished");
	}

	@GetMapping("/getdb")
	public void testController2(String table) {
		String path = System.getProperty("user.home") + File.separator + ".everyCloud" + File.separator + "EveryCloud.db";
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path+"?cipher=sqlcipher&legacy=4&key=test");
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
