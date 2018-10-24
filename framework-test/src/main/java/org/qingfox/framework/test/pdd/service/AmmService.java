package org.qingfox.framework.test.pdd.service;

import java.io.File;
import java.util.Properties;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.qingfox.framework.test.pdd.constant.PathConstant;
import org.qingfox.framework.test.pdd.constant.ServiceTypeConstant;
import org.qingfox.framework.test.pdd.constant.URLConstant;
import org.qingfox.framework.test.pdd.constant.XPathConstant;
import org.qingfox.framework.test.pdd.entity.GoodsEntity;
import org.qingfox.framework.test.pdd.service.inf.ICommissionService;
import org.qingfox.framework.test.pdd.utils.WebDriverUtils;

import com.framework.common.exceptions.ServiceException;
import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.common.utils.DateUtil;
import com.framework.common.utils.FileUtil;
import com.framework.common.utils.PropertiesUtil;
import com.framework.common.utils.ThreadUtil;

public class AmmService extends LoginService implements ICommissionService {
	private static final ILogger logger = LoggerFactory.getLogger(AmmService.class);

	private WebDriver driver;
	private String adzoneid;
	private String siteid;
	private Object lockObject = new Object();
	private Long heartTime = 60000 * 10L;

	public AmmService() throws Exception {
		Properties configPro = PropertiesUtil.loadProperties(new File(PathConstant.DIR_ROOT + File.separator + ServiceTypeConstant.AMM + File.separator + "config.properties"));
		this.adzoneid = configPro.getProperty("adzoneid");
		this.siteid = configPro.getProperty("siteid");

		this.driver = WebDriverUtils.getDriver(WebDriverUtils.TYPE_DRIVER_PHANTOMJS);
		this.driver.manage().window().maximize();
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
						if (driver.manage().getCookieNamed("login") != null) {
							setLogin(true);
							ThreadUtil.sleep(1000);
							setUsername(WebDriverUtils.waitXpath(driver, XPathConstant.AMM_INDEX_EL_USERNAME).getText().replace("你好，", ""));
							onLogin();
							continue;
						}
					} catch (Exception e) {
						logger.error(e, "等待用户扫描二维码失败");
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
			driver.get(URLConstant.AMM_LOGIN);

			logger.info("点击进入登陆按钮");
			WebDriverUtils.clickXpath(driver, XPathConstant.AMM_LOGIN_EL_LOGIN);

			logger.info("获取登陆frame");
			driver.switchTo().frame(WebDriverUtils.waitXpath(driver, XPathConstant.AMM_LOGIN_EL_FRAME));

			logger.info("获取二维码");
			String url = WebDriverUtils.waitXpath(driver, XPathConstant.AMM_LOGIN_EL_CODE).getAttribute("src");

			logger.info("下载二维码");
			File codeFile = new File(PathConstant.DIR_ROOT + File.separator + PathConstant.DIR_TEMP + File.separator + "amm_qr_" + DateUtil.getNowDate(DateUtil.PATTERN_YYYYMMDDHHMMSS) + ".jpg");
			if (!codeFile.getParentFile().exists()) {
				codeFile.getParentFile().mkdirs();
			}
			FileUtil.downloadFile(url, codeFile);
			onCodeChange(codeFile);
		}
	}

	public void login(String username, String password, String code) throws Exception {
		synchronized (lockObject) {

			logger.info("进入登陆页面");
			driver.get(URLConstant.AMM_LOGIN);

			logger.info("点击进入登陆按钮");
			WebDriverUtils.clickXpath(driver, XPathConstant.AMM_LOGIN_EL_LOGIN);

			logger.info("获取登陆frame");
			driver.switchTo().frame(WebDriverUtils.waitXpath(driver, XPathConstant.AMM_LOGIN_EL_FRAME));

			logger.info("点击切换登陆方式-用户名密码登陆");
			WebDriverUtils.clickXpath(driver, XPathConstant.AMM_LOGIN_EL_SWITCHPW);

			logger.info("输入用户名");
			WebDriverUtils.waitXpath(driver, XPathConstant.AMM_LOGIN_EL_USERNAME).clear();
			WebDriverUtils.waitXpath(driver, XPathConstant.AMM_LOGIN_EL_USERNAME).sendKeys(username);

			logger.info("输入密码");
			WebDriverUtils.waitXpath(driver, XPathConstant.AMM_LOGIN_EL_PASSWORD).clear();
			WebDriverUtils.waitXpath(driver, XPathConstant.AMM_LOGIN_EL_PASSWORD).sendKeys(password);

			boolean lock = false;
			try {
				WebElement lockEl = WebDriverUtils.waitXpath(driver, XPathConstant.AMM_LOGIN_EL_LOCK);
				if (lockEl.isDisplayed()) {
					lock = true;
				}
			} catch (Exception e) {
			}
			if (lock) {
				throw new ServiceException("碰到滑块");
			}

			logger.info("点击登陆");
			WebDriverUtils.clickXpath(driver, XPathConstant.AMM_LOGIN_EL_SUBMIT);

			ThreadUtil.sleep(2000);
			Cookie cookie = driver.manage().getCookieNamed("login");

			if (cookie == null) {
				throw new ServiceException("登录失败");
			} else {
				setUsername(username);
				setPassword(password);
				setLogin(true);
				onLogin();
			}
		}
	}

	@Override
	public String getCommission(GoodsEntity goods) throws Exception {
		checkLogin();
		synchronized (lockObject) {
			logger.info("开始获取推广连接URL");
			StringBuffer url = new StringBuffer(URLConstant.AMM_POP);
			url.append("&auctionid=" + goods.getBuyId());
			url.append("&adzoneid=" + this.adzoneid);
			url.append("&siteid=" + this.siteid);
			logger.debug("开始打开推广获取连接url【", url.toString(), "】");
			driver.get(url.toString());
			JSONObject responseJSON = JSONObject.parseObject(WebDriverUtils.waitTagname(driver, "pre").getText());
			String shortLinkUrl = responseJSON.getJSONObject("data").getString("shortLinkUrl");
			if (StringUtils.isEmpty(shortLinkUrl)) {
				throw new ServiceException("推广地址为空");
			}
			return shortLinkUrl;
		}
	}

	private void heart() {
		synchronized (lockObject) {
			driver.get(URLConstant.AMM_HEART);
			ThreadUtil.sleep(1000);
			if (WebDriverUtils.isForward(driver, URLConstant.AMM_HEART)) {
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
