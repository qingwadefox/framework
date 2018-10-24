/**
 * Copyright (c) 1987-2010 Fujian Fujitsu Communication Software Co., 
 * Ltd. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of 
 * Fujian Fujitsu Communication Software Co., Ltd. 
 * ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with FFCS.
 *
 * FFCS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. FFCS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.framework.test.pdd.service;

import java.io.File;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.framework.test.pdd.constant.ParamConstant;
import com.framework.test.pdd.constant.PathConstant;
import com.framework.test.pdd.constant.ServiceTypeConstant;
import com.framework.test.pdd.constant.URLConstant;
import com.framework.test.pdd.constant.XPathConstant;
import com.framework.test.pdd.entity.OrderEntity;
import com.framework.test.pdd.service.inf.IBuyService;
import com.framework.test.pdd.service.inf.IRegionMatchService;
import com.framework.test.pdd.service.listeners.LoginListener;
import com.framework.test.pdd.utils.WebDriverUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.framework.common.exceptions.ServiceException;
import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.common.utils.DateUtil;
import com.framework.common.utils.FileUtil;
import com.framework.common.utils.ThreadUtil;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see: @创建日期：2017年12月15日 @功能说明：
 * 
 */
public class TaobaoService extends LoginService implements IBuyService {

	private static final ILogger logger = LoggerFactory.getLogger(TaobaoService.class);

	private static Map<String, IRegionMatchService> regionMatchServiceMap;
	private WebDriver driver;
	private Object lockObject = new Object();
	private Long heartTime = 60000 * 10L;

	public TaobaoService() throws Exception {
		this.driver = WebDriverUtils.getDriver(WebDriverUtils.TYPE_DRIVER_CHROME);

		this.driver.manage().window().maximize();
		if (regionMatchServiceMap == null) {
			regionMatchServiceMap = new HashMap<String, IRegionMatchService>();
			regionMatchServiceMap.put(ServiceTypeConstant.PDD, new PddToTaobaoRegionMatchService(PathConstant.DIR_ROOT + File.separator + ServiceTypeConstant.TAOBAO));
		}
		// this.refreshCode();
		new Thread() {
			@Override
			public void run() {
				logger.info("等待用户扫描二维码");
				while (true) {
					ThreadUtil.sleep(1000);
					try {
						if (isLogin()) {
							continue;
						}
						if (WebDriverUtils.isForward(driver, URLConstant.TAOBAO_INDEX)) {
							setLogin(true);
							setUsername(WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_INDEX_EL_USERNAME).getText());
							onLogin();
							continue;
						}
					} catch (Exception e) {
						logger.error(e, "等到用户扫描二维码失败");
						continue;
					}
				}

			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				while (true) {
					ThreadUtil.sleep(heartTime);
					heart();
				}
			}

		}.start();
	}

	/**
	 * 刷新二维码 .
	 * 
	 * @throws Exception
	 * @author Administrator 2018年1月5日 Administrator
	 */
	public void refreshCode() throws Exception {
		synchronized (lockObject) {
			logger.info("开始刷新二维码");

			logger.info("进入登录页面");
			driver.get(URLConstant.TAOBAO_LOGIN);

			logger.info("获取二维码");
			String url = WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_LOGIN_IMG_QRCODE).getAttribute("src");

			logger.info("下载二维码");
			File codeFile = new File(PathConstant.DIR_ROOT + File.separator + PathConstant.DIR_TEMP + File.separator + "taobao_qr_" + DateUtil.getNowDate(DateUtil.PATTERN_YYYYMMDDHHMMSS) + ".jpg");
			if (!codeFile.getParentFile().exists()) {
				codeFile.getParentFile().mkdirs();
			}
			FileUtil.downloadFile(url, codeFile);
			onCodeChange(codeFile);
		}
	}

	/**
	 * 登录 {@inheritDoc}
	 * 
	 * @see com.framework.test.pdd.service.inf.IBuyService#login(java.lang.String,
	 *      java.lang.String)
	 * @author Administrator 2018年1月5日 Administrator
	 * @throws Exception
	 */
	public void login(String username, String password, String code) throws Exception {
		synchronized (lockObject) {
			logger.info("开始执行登录操作");

			logger.debug("username【", username, "】");
			if (StringUtils.isEmpty(username)) {
				throw new ServiceException("用户名为空");
			}

			logger.debug("password【", password, "】");
			if (StringUtils.isEmpty(password)) {
				throw new ServiceException("密码为空");
			}

			logger.info("进入登录页面");
			driver.get(URLConstant.TAOBAO_LOGIN);

			logger.info("点击切换登陆方式-用户名密码登陆");
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_LOGIN_EL_SWITCH);

			logger.info("设置用户名");
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_LOGIN_EL_USERNAME).clear();
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_LOGIN_EL_USERNAME).sendKeys(username);

			logger.info("设置密码");
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_LOGIN_EL_PASSWORD).clear();
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_LOGIN_EL_PASSWORD).sendKeys(password);

			boolean lock = false;
			try {
				WebElement lockEl = WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_LOGIN_EL_LOCK);
				if (lockEl.isDisplayed()) {
					lock = true;
				}
			} catch (Exception e) {
			}
			if (lock) {
				throw new ServiceException("碰到滑块");
			}

			logger.info("点击登陆");
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_LOGIN_BTN_SUBMIT);

			ThreadUtil.sleep(2000);
			boolean isForward = WebDriverUtils.isForward(driver, URLConstant.TAOBAO_MY);

			if (!isForward) {
				throw new ServiceException("登录失败");
			} else {
				logger.info("登录成功");
				setUsername(username);
				setPassword(password);
				setLogin(true);
				onLogin();
			}
		}
	}

	/**
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public void buy(OrderEntity order) throws Exception {
		checkLogin();
		synchronized (lockObject) {
			logger.info("购买订单");

			logger.info("验证省ID映射");
			String buyProvinceId = null;
			try {
				buyProvinceId = regionMatchServiceMap.get(order.getSellType()).getRegionId(order.getSellProvinceId());
			} catch (Exception e) {
				throw new ServiceException(e, "获取省映射ID失败");
			}
			logger.debug("buyProvinceId【", buyProvinceId, "】");
			if (StringUtils.isEmpty(buyProvinceId)) {
				throw new ServiceException("获取buyProvinceId为空");
			}
			order.setBuyProvinceId(buyProvinceId);

			logger.info("验证市ID映射");
			String buyCityId = null;
			try {
				buyCityId = regionMatchServiceMap.get(order.getSellType()).getRegionId(order.getSellCityId());
			} catch (Exception e) {
				throw new ServiceException(e, "验证市ID映射失败");
			}
			logger.debug("buyCityId【", buyCityId, "】");
			if (StringUtils.isEmpty(buyCityId)) {
				throw new ServiceException("获取buyCityId为空");
			}
			order.setBuyCityId(buyCityId);

			logger.info("验证区ID映射");
			String buyDistrictId = null;
			try {
				buyDistrictId = regionMatchServiceMap.get(order.getSellType()).getRegionId(order.getSellDistrictId());
			} catch (Exception e) {
				logger.warn("获取市ID失败");
			}
			logger.debug("buyDistrictId【", buyDistrictId, "】");
			order.setBuyDistrictId(buyDistrictId);

			logger.info("跳转详情页面");
			driver.get(URLConstant.AMM_ITEM_BUY + order.getBuyUrl());

			if (!WebDriverUtils.isForward(driver, URLConstant.TAOBAO_ITEM_BUY)) {
				throw new ServiceException("打开购买页面失败");
			}
			ThreadUtil.sleep(1000);
			logger.info("选择SKU");
			if (StringUtils.isEmpty(order.getBuySkuId())) {
				throw new ServiceException("skuId为空");

			}
			if (!order.getBuySkuId().equals("def")) {
				String[] skus = order.getBuySkuId().split(";");
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				for (String sku : skus) {

					WebElement skuElement = WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_DETAILS_EL_SKU.replace(ParamConstant.DEFAULT_PARAM, sku));
					if (skuElement.getAttribute("class").indexOf("tb-selected") == -1) {
						jse.executeScript("arguments[0].click();", skuElement);

					}
				}
			}

			logger.info("点击购买");
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_DETAILS_EL_BUY).click();

			boolean lock = false;
			try {
				WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_EL_LOCK);
				lock = true;
			} catch (Exception e) {
			}
			if (lock) {
				throw new ServiceException("碰到滑块");
			}

			logger.info("判断跳转购买信息界面");
			if (!WebDriverUtils.isForward(driver, URLConstant.TAOBAO_BUY)) {
				throw new ServiceException("跳转购买信息页面失败");
			}

			logger.info("点击编辑地址");
			try {

				WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_EL_ADDRESSEDIT);
			} catch (Exception e) {
				logger.warn(e, "点击编辑地址失败，尝试点击马上完善地址");
				WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_EL_ADDRESSEDITOTHER);

			}

			ThreadUtil.sleep(500);
			driver.switchTo().frame(WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_EL_ADDRESSEDITFRAME));

			logger.info("点击省市区街道选择");
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_EDIT);

			logger.info("选择省");
			ThreadUtil.sleep(500);
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_PROVINCE);
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_VALUE.replace(ParamConstant.DEFAULT_PARAM, order.getBuyProvinceId()));

			logger.info("选择市");
			ThreadUtil.sleep(500);
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_CITY);
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_VALUE.replace(ParamConstant.DEFAULT_PARAM, order.getBuyCityId()));

			logger.info("选择区");
			ThreadUtil.sleep(500);
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_DISTRICT);
			if (StringUtils.isEmpty(order.getBuyDistrictId())) {
				WebElement districtElement = WebDriverUtils.xpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_DISTRICT_CONTENT);
				if (StringUtils.isNotEmpty(districtElement.getText().trim())) {
					WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_DISTRICT_FIRST);
				}
			} else {
				WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_VALUE.replace(ParamConstant.DEFAULT_PARAM, order.getBuyDistrictId()));
			}

			logger.info("选择街道");
			ThreadUtil.sleep(500);
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_STREET);
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_VALUE.replace(ParamConstant.DEFAULT_PARAM, "-1"));

			logger.info("填写详细地址");
			ThreadUtil.sleep(500);
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_ADDRESS).clear();
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_ADDRESS).sendKeys(order.getSellAddress());

			logger.info("填写收货人姓名");
			ThreadUtil.sleep(500);
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_NAME).clear();
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_NAME).sendKeys(order.getSellReceiveName());

			logger.info("输入手机号码");
			ThreadUtil.sleep(500);
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_MOBILE).clear();
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_MOBILE).sendKeys(order.getSellReceiveMobile());

			logger.info("点击省市区街道选择");
			ThreadUtil.sleep(500);
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_EDIT);

			logger.info("再次选择街道");
			ThreadUtil.sleep(500);
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_STREET);

			logger.info("点击选择第一个街道");
			ThreadUtil.sleep(500);
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_STREET_FIRST);

			logger.info("提交地址");
			ThreadUtil.sleep(500);
			WebDriverUtils.clickXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_BTN_SUBMIT);

			ThreadUtil.sleep(1000);
			driver.switchTo().defaultContent();

			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_EL_NUMBER).clear();
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_EL_NUMBER).sendKeys(order.getSellGoodsNumber());

			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_EL_DETAIL).clear();
			WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_EL_DETAIL).sendKeys("pdd:" + order.getSellOrderId());

			ThreadUtil.sleep(1000);
			String realPay = WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ITEM_BUY_EL_REALPAY).getText();
			logger.debug("获取买单金额【", realPay, "】");
			Float sellAmount = Float.parseFloat(order.getSellAmount()) / 100;
			if (sellAmount < Float.parseFloat(realPay)) {
				throw new ServiceException("卖单金额【", sellAmount, "】大于买单金额【", realPay, "】不对应");
			}

			ThreadUtil.sleep(2000);
			logger.info("提交订单");
			WebDriverUtils.waitXpath(driver, "//*[@id='submitOrder_1']/div[1]/a").click();
		}
	}

	/**
	 * 获取订单列表 {@inheritDoc}
	 * 
	 * @see com.framework.test.pdd.service.inf.IBuyService#getOrderList(java.util.List)
	 * @author Administrator 2018年1月25日 Administrator
	 */
	public List<OrderEntity> getOrderList(List<OrderEntity> list) throws Exception {
		checkLogin();
		synchronized (lockObject) {
			OrderEntity lastOrder = list.get(list.size() - 1);
			if (lastOrder.getSellOrderState().equals(OrderEntity.SELLORDERSTATE_UNGROUP)) {
				return list;
			}
			Calendar lastOrderTime = Calendar.getInstance();
			lastOrderTime.setTimeInMillis(Long.parseLong(lastOrder.getSellOrderTime() + "000"));
			lastOrderTime.set(Calendar.HOUR_OF_DAY, 0);
			lastOrderTime.set(Calendar.SECOND, 0);
			lastOrderTime.set(Calendar.MINUTE, 0);

			driver.get(URLConstant.TAOBAO_ORDER_LIST);
			List<WebElement> orderListElement = WebDriverUtils.waitXpaths(driver, XPathConstant.TAOBAO_ORDER_LIST_EL_DEST);

			while (true) {

				boolean over = false;
				for (WebElement orderElement : orderListElement) {
					String buyOrderTime = orderElement.findElement(By.xpath(XPathConstant.TAOBAO_ORDER_LIST_DEST_TIME)).getText();
					Long buyOrderTimeMils = DateUtil.parse(buyOrderTime).getTime();
					System.out.println(DateUtil.format(buyOrderTimeMils) + " - " + DateUtil.format(lastOrderTime));
					if (buyOrderTimeMils < lastOrderTime.getTimeInMillis()) {
						over = true;
						break;
					}

					String buyOrderState = orderElement.findElement(By.xpath(XPathConstant.TAOBAO_ORDER_LIST_DEST_STATE)).getText();
					logger.debug("buyOrderState【", buyOrderState, "】");
					if (buyOrderState.equals("交易关闭")) {
						continue;
					}

					String buyOrderId = orderElement.findElement(By.xpath(XPathConstant.TAOBAO_ORDER_LIST_DEST_ID)).getText();
					logger.debug("buyOrderId【", buyOrderId, "】");

					String sellOrderId = null;
					try {
						((JavascriptExecutor) driver).executeScript("window.open(arguments[0]);", URLConstant.TAOBAO_ORDER_MSG.replace(ParamConstant.DEFAULT_PARAM, buyOrderId));
						ThreadUtil.sleep(1000);
						WebDriverUtils.switchWindow(driver, driver.getWindowHandles().size() - 1);
						sellOrderId = JSONObject.parseObject(WebDriverUtils.waitTagname(driver, "body").getText()).getString("tip").replace("留言:pdd:", "");
						logger.debug("sellOrderId【", sellOrderId, "】");
					} catch (Exception e) {
						continue;
					} finally {
						WebDriverUtils.closeWindow(driver, driver.getWindowHandles().size() - 1);
					}
					if (StringUtils.isEmpty(sellOrderId) || sellOrderId.equals("无")) {
						continue;
					}

					String buyExpressId = null;
					String buyExpressName = null;
					if (buyOrderState.equals("物流运输中") || buyOrderState.equals("卖家已发货") || buyOrderState.equals("快件已揽收")) {
						try {
							((JavascriptExecutor) driver).executeScript("window.open(arguments[0]);", URLConstant.TAOBAO_ORDER_LOGISTICS.replace(ParamConstant.DEFAULT_PARAM, buyOrderId));
							ThreadUtil.sleep(1000);
							WebDriverUtils.switchWindow(driver, driver.getWindowHandles().size() - 1);
							JSONObject logisticsJson = JSONObject.parseObject(WebDriverUtils.waitTagname(driver, "pre").getText());
							buyExpressId = logisticsJson.getString("expressId");
							buyExpressName = logisticsJson.getString("expressName");
						} catch (Exception e) {
							logger.error(e, "获取物流信息失败");
						} finally {
							WebDriverUtils.closeWindow(driver, driver.getWindowHandles().size() - 1);
						}
						logger.debug("buyExpressId【", buyExpressId, "】");
						logger.debug("buyExpressName【", buyExpressName, "】");
					}

					String buyOrderPrice = orderElement.findElement(By.xpath(XPathConstant.TAOBAO_ORDER_LIST_DEST_PRICE)).getText();
					logger.debug("buyOrderPrice【", buyOrderPrice, "】");

					for (OrderEntity order : list) {
						if (order.getSellOrderState().equals(OrderEntity.SELLORDERSTATE_UNGROUP)) {
							continue;
						}
						if (order.getSellOrderId().equals(sellOrderId)) {
							order.setBuyOrderState(OrderEntity.textTobuyOrderState(buyOrderState));
							order.setBuyAmount(buyOrderPrice);
							order.setBuyOrderTime(buyOrderTime);
							order.setBuyOrderId(buyOrderId);
							order.setBuyExpressId(buyExpressId);
							order.setBuyExpressName(buyExpressName);
							break;
						}
					}

				}

				if (over) {
					break;
				}

				WebElement nextElement = WebDriverUtils.waitXpath(driver, XPathConstant.TAOBAO_ORDER_LIST_NEXT);
				if (nextElement.getAttribute("class").startsWith("pagination-disabled")) {
					break;
				} else {
					nextElement.click();
				}
			}
			return list;
		}
	}

	private void heart() {
		synchronized (lockObject) {
			driver.get(URLConstant.TAOBAO_HEART);
			ThreadUtil.sleep(1000);
			if (WebDriverUtils.isForward(driver, URLConstant.TAOBAO_HEART)) {
				if (!isLogin()) {
					setLogin(true);
					onLogin();
				}
			} else {
				if (isLogin()) {
					setLogin(false);
					onLogout();
				}
			}
		}
	}

}
