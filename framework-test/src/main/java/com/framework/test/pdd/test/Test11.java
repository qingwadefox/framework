/**
 * 
 */
package com.framework.test.pdd.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.framework.test.pdd.service.AmmDatamineService;

/**
 * @author Administrator
 *
 */
public class Test11 {
	public static void main(String[] args) throws Exception {

		AmmDatamineService ads = new AmmDatamineService();
		System.out.println(ads.getGoodsCommission("537596751121"));

	}
}
