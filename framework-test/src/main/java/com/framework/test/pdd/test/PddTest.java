/**
 * 
 */
package com.framework.test.pdd.test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.framework.test.pdd.service.PddService;
import com.framework.test.pdd.service.listeners.LoginListener;

/**
 * @author zhengwei
 *
 */
public class PddTest {
	public static void main(String[] args) {
		try {
			PddService pddService = new PddService();
			pddService.addLoginListener(new LoginListener() {
				@Override
				public void onLogin(Object source) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onCodeChange(Object source, File codeFile) {
					try {
						Desktop.getDesktop().open(codeFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onLogout(Object sourcer) {
					// TODO Auto-generated method stub

				}
			});
			pddService.refreshCode();

			Scanner sc = new Scanner(System.in);
			String code = sc.nextLine(); // 读取字符串型输入
			pddService.login("15606010895", "Iloveyou1985", code);
			pddService.chat();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
