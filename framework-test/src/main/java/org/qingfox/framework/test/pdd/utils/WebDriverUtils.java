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
package org.qingfox.framework.test.pdd.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qingfox.framework.test.pdd.constant.PathConstant;

import org.qingfox.framework.common.utils.RandomUtil;
import org.qingfox.framework.common.utils.ThreadUtil;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see: @创建日期：2017年12月15日 @功能说明：
 * 
 */
public class WebDriverUtils {
    private final static int FORWARD_TRY_COUNT = 5;
    private final static long ELEMENT_WAITE_TIME = 10;

    private final static int SELECT_BY_ID = 0;
    private final static int SELECT_BY_VALUE = 1;
    private final static int SELECT_BY_VISIBLETEXT = 2;

    public final static int TYPE_DRIVER_CHROME = 0;
    public final static int TYPE_DRIVER_IE = 1;
    public final static int TYPE_DRIVER_FIREFOX = 2;
    public final static int TYPE_DRIVER_PHANTOMJS = 3;

    public static final String DRIVER_FIREFOX_PATH = PathConstant.DIR_ROOT + "/driver/geckodriver.exe";
    public static final String DRIVER_ID_PATH = PathConstant.DIR_ROOT + "/driver/IEDriverServer.exe";
    public static final String DRIVER_CHROME_PATH = PathConstant.DIR_ROOT + "/driver/chromedriver.exe";
    public static final String DRIVER_PHANTOMJS_PATH = PathConstant.DIR_ROOT + "/driver/phantomjs.exe";

    public static WebDriver getDriver(Integer driverType) {
        switch (driverType) {
        case TYPE_DRIVER_CHROME:
            System.setProperty("webdriver.chrome.driver", DRIVER_CHROME_PATH);
            return new ChromeDriver();
        case TYPE_DRIVER_IE:
            System.setProperty("webdriver.ie.driver", DRIVER_ID_PATH);
            return new InternetExplorerDriver();
        case TYPE_DRIVER_FIREFOX:
            System.setProperty("webdriver.gecko.driver", DRIVER_FIREFOX_PATH);
            return new FirefoxDriver();
        case TYPE_DRIVER_PHANTOMJS:
            System.setProperty("phantomjs.binary.path", DRIVER_PHANTOMJS_PATH);
            return new PhantomJSDriver();
        default:
            return null;
        }
    }

    /**
     * 根据TAGNAME获取
     * 
     * @param driver
     * @param tagname
     * @return
     */
    public static WebElement tagname(WebDriver driver, String tagname) {
        return driver.findElement(By.tagName(tagname));
    }

    /**
     * 根据TAGNAME获取
     * 
     * @param driver
     * @param tagname
     * @return
     */
    public static List<WebElement> tagnames(WebDriver driver, String tagname) {
        return driver.findElements(By.tagName(tagname));
    }

    /**
     * 根据tagname等待
     * 
     * @param driver
     * @param tagname
     * @return
     */
    public static WebElement waitTagname(WebDriver driver, String tagname) {
        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_WAITE_TIME);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(tagname)));
        return tagname(driver, tagname);
    }

    /**
     * 根据tagname等待
     * 
     * @param driver
     * @param tagname
     * @return
     */
    public static List<WebElement> waitTagnames(WebDriver driver, String tagname) {
        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_WAITE_TIME);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(tagname)));
        return tagnames(driver, tagname);
    }

    /**
     * 根据名称获取 .
     * 
     * @param driver
     * @param name
     * @return
     * @author Administrator 2017年12月15日 Administrator
     */
    public static WebElement name(WebDriver driver, String name) {
        return driver.findElement(By.name(name));
    }

    /**
     * 根据名称点击 .
     * 
     * @param driver
     * @param name
     * @author Administrator 2017年12月15日 Administrator
     */
    public static void clickName(WebDriver driver, String name) {
        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_WAITE_TIME);
        wait.until(ExpectedConditions.elementToBeClickable(By.name(name)));
        name(driver, name).click();
    }

    /**
     * 根据ID获取 .
     * 
     * @param driver
     * @param id
     * @return
     * @author Administrator 2017年12月15日 Administrator
     */
    public static WebElement id(WebDriver driver, String id) {
        return driver.findElement(By.id(id));
    }

    /**
     * 根据ID点击 .
     * 
     * @param driver
     * @param id
     * @author Administrator 2017年12月15日 Administrator
     */
    public static void clickId(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_WAITE_TIME);
        wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        id(driver, id).click();
    }

    /**
     * 根据xpath获取 .
     * 
     * @param driver
     * @param xpath
     * @return
     * @author Administrator 2017年12月15日 Administrator
     */
    public static WebElement xpath(WebDriver driver, String xpath) {
        return driver.findElement(By.xpath(xpath));
    }

    /**
     * 根据xpath获取 .
     * 
     * @param driver
     * @param xpath
     * @return
     * @author Administrator 2017年12月18日 Administrator
     */
    public static List<WebElement> xpaths(WebDriver driver, String xpath) {
        return driver.findElements(By.xpath(xpath));
    }

    /**
     * 根据xpath获取 - 等待 .
     * 
     * @param driver
     * @param xpath
     * @return
     * @author Administrator 2017年12月18日 Administrator
     */
    public static WebElement waitXpath(WebDriver driver, String xpath) {
        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_WAITE_TIME);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        return xpath(driver, xpath);
    }

    /**
     * 根据xpath获取 - 等待 .
     * 
     * @param driver
     * @param xpath
     * @return
     * @author Administrator 2017年12月18日 Administrator
     */
    public static List<WebElement> waitXpaths(WebDriver driver, String xpath) {
        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_WAITE_TIME);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        return xpaths(driver, xpath);
    }

    /**
     * 根据xpath点击 .
     * 
     * @param driver
     * @param id
     * @author Administrator 2017年12月15日 Administrator
     */
    public static void clickXpath(WebDriver driver, String xpath) {
        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_WAITE_TIME);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        xpath(driver, xpath).click();
    }

    /**
     * 根据CSS获取 .
     * 
     * @param driver
     * @param css
     * @return
     * @author Administrator 2017年12月15日 Administrator
     */
    public static WebElement css(WebDriver driver, String css) {
        return driver.findElement(By.className(css));
    }

    /**
     * 根据CSS点击 .
     * 
     * @param driver
     * @param xpath
     * @author Administrator 2017年12月15日 Administrator
     */
    public static void clickCss(WebDriver driver, String css) {
        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_WAITE_TIME);
        wait.until(ExpectedConditions.elementToBeClickable(By.className(css)));
        css(driver, css).click();
    }

    public static WebElement waitName(WebDriver driver, String name) {
        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_WAITE_TIME);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(name)));
        return name(driver, name);
    }

    public static WebElement waitId(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_WAITE_TIME);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
        return id(driver, id);
    }

    public static WebElement waitCss(WebDriver driver, String css) {
        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_WAITE_TIME);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(css)));
        return css(driver, css);
    }

    public static void select(WebElement selectEl, String value) {
        select(selectEl, value, SELECT_BY_VALUE);
    }

    public static void select(WebElement selectEl, String value, int type) {
        Select select = new Select(selectEl);
        switch (type) {
        case SELECT_BY_ID:
            select.selectByIndex(Integer.parseInt(value));
            break;
        case SELECT_BY_VALUE:
            select.selectByValue(value);
            break;
        case SELECT_BY_VISIBLETEXT:
            select.selectByVisibleText(value);
            break;
        }
    }

    public static String pageState(WebDriver driver) {
        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString();
    }

    public static boolean isForward(WebDriver driver, String url) {
        int tryCount = 0;
        while (tryCount < FORWARD_TRY_COUNT && driver.getCurrentUrl().indexOf(url) == -1) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tryCount++;
        }
        if (tryCount < FORWARD_TRY_COUNT) {
            return true;
        } else {
            return false;
        }
    }

    public static void openWindow(WebDriver driver, String url) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.open('" + url + "');");
        switchWindow(driver, driver.getWindowHandles().size() - 1);
    }

    public static void switchWindow(WebDriver driver, Integer index) {
        Iterator<String> it = driver.getWindowHandles().iterator();
        int i = 0;
        while (it.hasNext()) {
            String hd = it.next();
            if (index == i) {
                driver.switchTo().window(hd);
                break;
            }
            i++;
        }
    }

    public static void closeWindow(WebDriver driver, Integer index) {
        if (index >= driver.getWindowHandles().size()) {
            return;
        }
        Iterator<String> it = driver.getWindowHandles().iterator();
        int i = 0;
        while (it.hasNext()) {
            String hd = it.next();
            if (index == i) {
                driver.switchTo().window(hd).close();
                break;
            }
            i++;
        }
        if (index > 0) {
            switchWindow(driver, index - 1);
        }
    }

    public static void leaveFirstWindow(WebDriver driver) {
        Iterator<String> it = driver.getWindowHandles().iterator();
        String first = null;
        while (it.hasNext()) {
            String hd = it.next();
            if (first == null) {
                first = hd;
            } else {
                driver.switchTo().window(hd).close();
            }
        }

        driver.switchTo().window(first);
    }

    public static void closeWindow(WebDriver driver, String id) {
        driver.switchTo().window(id).close();
    }

    // /**
    // * 关闭最后一个窗口
    // *
    // * @param driver
    // */
    // public static void closeLastWindow(WebDriver driver) {
    // Iterator<String> it = driver.getWindowHandles().iterator();
    // String lastHD = null;
    // while (it.hasNext()) {
    // lastHD = it.next();
    // }
    // if (lastHD != null) {
    // driver.switchTo().window(lastHD).close();
    // }
    // }

    public static void move(WebDriver driver, WebElement element, int distance) throws InterruptedException {
        int xDis = distance + 11;
        System.out.println("应平移距离：" + xDis);
        int moveX = new Random().nextInt(8) - 5;
        int moveY = 1;
        Actions actions = new Actions(driver);
        new Actions(driver).clickAndHold(element).perform();
        Thread.sleep(200);
        printLocation(element);
        actions.moveToElement(element, moveX, moveY).perform();
        System.out.println(moveX + "--" + moveY);
        printLocation(element);
        for (int i = 0; i < 22; i++) {
            int s = 10;
            if (i % 2 == 0) {
                s = -10;
            }
            actions.moveToElement(element, s, 1).perform();
            Thread.sleep(new Random().nextInt(100) + 150);
        }
        System.out.println(xDis + "--" + 1);
        actions.moveByOffset(xDis, 1).perform();
        printLocation(element);
        Thread.sleep(200);
        actions.release(element).perform();
    }

    public static void printCookie(WebDriver driver) {
        Iterator<Cookie> it = driver.manage().getCookies().iterator();
        while (it.hasNext()) {
            System.out.println(it.next().getName());
        }
    }

    public static void deblocking(WebDriver driver, WebElement source) {
        Actions action = new Actions(driver);
        action.clickAndHold(source).perform();
        for (int i : RandomUtil.randomSplitNumber(RandomUtil.randomNumber(261, 279), 50, 70)) {
            action.moveByOffset(i, RandomUtil.randomNumber(3, 20)).perform();
            ThreadUtil.sleep(1);
        }
        action.release().perform();
        ThreadUtil.sleep(500);
    }

    private static void printLocation(WebElement element) {
        Point point = element.getLocation();
        System.out.println(point.toString());
    }

}
