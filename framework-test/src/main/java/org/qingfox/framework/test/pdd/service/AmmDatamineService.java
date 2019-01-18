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
package org.qingfox.framework.test.pdd.service;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.qingfox.framework.test.pdd.constant.PathConstant;
import org.qingfox.framework.test.pdd.constant.ServiceTypeConstant;
import org.qingfox.framework.test.pdd.constant.URLConstant;
import org.qingfox.framework.test.pdd.constant.XPathConstant;
import org.qingfox.framework.test.pdd.entity.GoodsEntity;
import org.qingfox.framework.test.pdd.entity.MultipleSkuEntity;
import org.qingfox.framework.test.pdd.entity.SimpleSkuEntity;
import org.qingfox.framework.test.pdd.service.inf.IDatamineService;
import org.qingfox.framework.test.pdd.utils.WebDriverUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.qingfox.framework.common.log.ILogger;
import org.qingfox.framework.common.log.LoggerFactory;
import org.qingfox.framework.common.utils.DateUtil;
import org.qingfox.framework.common.utils.FileUtil;
import org.qingfox.framework.common.utils.PropertiesUtil;
import org.qingfox.framework.common.utils.ReadLine;
import org.qingfox.framework.common.utils.StrUtil;
import org.qingfox.framework.common.utils.ThreadUtil;
import org.qingfox.framework.common.utils.URIUtil;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see: @创建日期：2018年1月12日 @功能说明：
 * 
 */
public class AmmDatamineService extends LoginService implements IDatamineService {

    private static final ILogger logger = LoggerFactory.getLogger(AmmDatamineService.class);

    private WebDriver driver;

    private List<List<String>> cacheGoodsList;
    private Map<String, String> cacheGoodsMap;

    private Map<String, String> typeUrlMap;
    private List<String> excludeSkuIdList;
    private Integer size;
    private Integer maxPage;

    public AmmDatamineService() throws Exception {
        this.driver = WebDriverUtils.getDriver(WebDriverUtils.TYPE_DRIVER_PHANTOMJS);
        this.driver.manage().window().maximize();
        this.typeUrlMap = PropertiesUtil.loadMap(new File(PathConstant.DIR_ROOT + File.separator + ServiceTypeConstant.AMM + File.separator + "selltype_url.properties"));
        this.size = 100;
        this.maxPage = 100;
        this.excludeSkuIdList = new ArrayList<String>();
        this.excludeSkuIdList.add("-1:-1");
        this.cacheGoodsList = new ArrayList<List<String>>(100);
        this.cacheGoodsMap = new HashMap<String, String>();
    }

    @Override
    public void login(String username, String password, String code) throws Exception {
        this.setLogin(true);
        this.onLogin();
    }

    @Override
    public void refreshCode() throws Exception {

    }

    @Override
    public List<String> getGoodsList(String type, Integer page) throws Exception {
        this.checkLogin();

        // 查询页码是否超过最大值
        if (page == null || page <= 0) {
            throw new RuntimeException("页数【" + maxPage + "】有误");
        }

        // 超过最大返回空
        if (page > maxPage) {
            return new ArrayList<String>();
        }

        // 从程序缓存中读取数据
        List<String> goodsList = new ArrayList<>();

        if (this.cacheGoodsList.size() >= page) {
            goodsList = this.cacheGoodsList.get(page - 1);
            if (goodsList != null && !goodsList.isEmpty()) {
                return goodsList;
            }
        }

        // 从文件缓存中读取数据
        File file = new File(PathConstant.DIR_ROOT + File.separator + PathConstant.DIR_CACHE + File.separator + DateUtil.getNowDate(DateUtil.PATTERN_YYYYMMDD) + File.separator + "datamine_" + type + "_" + page + ".dat");
        if (file.exists()) {
            goodsList = new ArrayList<String>();
            FileUtil.readLine(file, new ReadLine<List<String>>() {
                @Override
                public void nextLine(String line, int number) {
                    GoodsEntity goods = JSONObject.parseObject(line, GoodsEntity.class);
                    getParam().add(line);
                    cacheGoodsMap.put(goods.getBuyId(), line);
                }
            }.setParam(goodsList));
            this.cacheGoodsList.add(page - 1, goodsList);
            logger.debug("获取到推广商品共【", goodsList.size(), "】条");
            return goodsList;
        }

        // 从页面中读取数据
        WebDriverUtils.leaveFirstWindow(driver);
        logger.info("开始获取推广商品列表");
        String urlStr = typeUrlMap.get(type);
        if (StringUtils.isEmpty(urlStr)) {
            throw new RuntimeException("商品爬取地址为空");
        }
        StringBuffer url = new StringBuffer(urlStr);
        url.append("&toPage=" + page);
        url.append("&perPageSize=" + size);
        logger.debug("url【", url.toString(), "】");
        logger.info("开始打开商品页面");
        driver.get(url.toString());
        ThreadUtil.sleep(1000);
        List<WebElement> resultElementList = WebDriverUtils.waitXpaths(driver, XPathConstant.AMM_SEARCH_LIST);
        List<GoodsEntity> _goodsList = new ArrayList<GoodsEntity>();
        for (WebElement resultElement : resultElementList) {
            GoodsEntity goods = new GoodsEntity();
            String buyId = URIUtil.getUrlParam(resultElement.findElement(By.xpath(XPathConstant.AMM_SEARCH_LIST_ID)).getAttribute("href"), "id");
            goods.setBuyId(buyId);
            StrUtil.readLine(resultElement.getText(), new ReadLine<GoodsEntity>() {
                @Override
                public void nextLine(String line, int number) {
                    if (line.indexOf("佣金：￥") != -1) {
                        getParam().setCommission(Float.parseFloat(line.replace("佣金：￥", "")));
                    }
                }
            }.setParam(goods));

            _goodsList.add(goods);
            logger.debug("buyId【", goods.getBuyId(), "】,commission【", goods.getCommission(), "】");
        }

        for (GoodsEntity goods : _goodsList) {
            try {
                goods = this.getGoods(goods);
                FileUtil.appendLine(file, JSONObject.toJSONString(goods));
                String goodsStr = JSONObject.toJSONString(goods);
                cacheGoodsMap.put(goods.getBuyId(), goodsStr);
                goodsList.add(goodsStr);
            } catch (Exception e) {
                logger.error(e, "获取buyId【", goods.getBuyId(), "】详情失败");
            }
        }
        this.cacheGoodsList.add(page - 1, goodsList);
        logger.debug("获取到推广商品共【", goodsList.size(), "】条");
        return goodsList;
    }

    @Override
    public Float getGoodsCommission(String buyId) throws Exception {
        String goodEntityStr = cacheGoodsMap.get(buyId);
        if (StringUtils.isNotEmpty(goodEntityStr)) {
            GoodsEntity goodsEntity = JSONObject.parseObject(goodEntityStr, GoodsEntity.class);
            return goodsEntity.getCommission();
        }

        String itemUrl = URLEncoder.encode(URLConstant.AMM_ITEMDETAILS + "?id=" + buyId, "UTF-8");
        String url = StrUtil.repParam(URLConstant.AMM_COMMISSION_SEARCH, itemUrl, System.currentTimeMillis());
        driver.get(url);
        try {
            List<WebElement> searchResult = WebDriverUtils.waitXpaths(driver, XPathConstant.AMM_SEARCH_LIST);
            if (searchResult.size() > 0) {
                List<Float> resultList = new ArrayList<>();
                StrUtil.readLine(searchResult.get(0).getText(), new ReadLine<List<Float>>() {
                    @Override
                    public void nextLine(String line, int number) {
                        if (line.indexOf("佣金：￥") != -1) {
                            getParam().add((Float.parseFloat(line.replace("佣金：￥", ""))));
                        }
                    }
                }.setParam(resultList));
                return resultList.get(0);
            }
        } catch (Exception e) {
        }

        return null;

    }

    private GoodsEntity getGoods(GoodsEntity goods) throws Exception {
        this.checkLogin();
        WebDriverUtils.leaveFirstWindow(driver);
        logger.info("开始爬取商品详情");
        goods.setBuyType(ServiceTypeConstant.TAOBAO);
        driver.get(URLConstant.AMM_ITEMDETAILS + "?id=" + goods.getBuyId());
        String buyCatId = WebDriverUtils.waitXpath(driver, XPathConstant.AMM_ITEMDETAILS_GOODS_DIV).getAttribute("data-catid");
        logger.debug("商品分类为【", goods.getBuyCatId(), "】");
        if (StringUtils.isEmpty(buyCatId)) {
            logger.warn("商分类为空");
        }
        goods.setBuyCatId(buyCatId);

        String name = WebDriverUtils.waitXpath(driver, XPathConstant.AMM_ITEMDETAILS_GOODS_NAME).getAttribute("data-title");
        logger.debug("商品标题为【", name, "】");
        if (StringUtils.isEmpty(name)) {
            throw new RuntimeException("商品标题为空");
        }
        goods.setName(name);

        String desc = WebDriverUtils.waitXpath(driver, XPathConstant.AMM_ITEMDETAILS_GOODS_DESC).getText();
        logger.debug("商品描述为【", desc, "】");
        if (StringUtils.isEmpty(desc)) {
            logger.warn("商品描述为空");
        }
        goods.setDesc(name);

        String currentprice = WebDriverUtils.waitXpath(driver, XPathConstant.AMM_ITEMDETAILS_GOODS_CURRENTPRICE).getAttribute("value");
        logger.debug("商品原价为【", currentprice, "】");
        if (StringUtils.isEmpty(currentprice)) {
            throw new RuntimeException("商品原价为空");
        }
        goods.setCurrentprice(currentprice);

        logger.info("开始爬取页面sku信息");
        Map<String, SimpleSkuEntity> simpleSkuMap = new HashMap<String, SimpleSkuEntity>();
        List<String> multipleSkuIdList = new ArrayList<String>();

        try {
            List<WebElement> skuDlEls = WebDriverUtils.waitXpaths(driver, XPathConstant.AMM_TEMDETAILS_GOODS_SKU);
            boolean isFirst = true;
            List<List<String>> simpleSkuIdList = new ArrayList<List<String>>();
            for (WebElement skuDlEl : skuDlEls) {
                List<String> _simpleSkuIdList = new ArrayList<String>();
                String css = skuDlEl.getAttribute("class");
                if (css.indexOf("J_Prop") == 0) {
                    isFirst = false;
                    String parentText = skuDlEl.findElement(By.xpath(XPathConstant.AMM_TEMDETAILS_GOODS_SKU_PARENT)).getAttribute("data-property");
                    logger.debug("获取到父级sku名称【", parentText, "】");
                    List<WebElement> skuElList = skuDlEl.findElements(By.xpath(XPathConstant.AMM_TEMDETAILS_GOODS_SKU_ALL));
                    for (WebElement skuEl : skuElList) {
                        String dataValue = skuEl.getAttribute("data-value");
                        if (excludeSkuIdList.contains(dataValue)) {
                            continue;
                        }
                        WebElement aEl = skuEl.findElement(By.tagName("a"));
                        String dataText = aEl.getText();
                        String dataImg = aEl.getAttribute("style");
                        if (StringUtils.isNotEmpty(dataImg)) {
                            dataImg = dataImg.substring(dataImg.indexOf("url(") + 4, dataImg.indexOf(")"));
                            dataImg = dataImg.substring(0, dataImg.lastIndexOf("_")) + "_480x480.jpg";
                        }
                        SimpleSkuEntity skuEntity = new SimpleSkuEntity();
                        skuEntity.setTaobaoSkuId(dataValue);
                        skuEntity.setTaobaoSkuText(dataText);
                        skuEntity.setTaobaoSkuImgUrl(StringUtils.isEmpty(dataImg) ? "" : dataImg);
                        skuEntity.setTaobaoSkuParentText(parentText);
                        logger.debug("获取到sku信息【", JSONObject.toJSONString(skuEntity), "】");
                        simpleSkuMap.put(dataValue, skuEntity);
                        _simpleSkuIdList.add(dataValue);
                    }
                    if (!_simpleSkuIdList.isEmpty()) {
                        simpleSkuIdList.add(_simpleSkuIdList);
                    }
                } else if (css.equals("tb-amount tb-clear")) {
                    if (isFirst) {
                        SimpleSkuEntity skuEntity = new SimpleSkuEntity();
                        skuEntity.setTaobaoSkuId("def");
                        simpleSkuMap.put("def", skuEntity);
                        _simpleSkuIdList.add("def");
                        simpleSkuIdList.add(_simpleSkuIdList);
                        logger.debug("不存在SKU信息，增加默认SKU信息");
                    }
                    break;
                }
            }

            if (simpleSkuIdList.size() == 1) {
                for (String skuId : simpleSkuIdList.get(0)) {
                    multipleSkuIdList.add(skuId);
                }
            } else if (simpleSkuIdList.size() == 2) {
                for (String skuId : simpleSkuIdList.get(0)) {
                    for (String _skuId : simpleSkuIdList.get(1)) {
                        multipleSkuIdList.add(skuId + ";" + _skuId);
                    }
                }
            } else {
                throw new RuntimeException("SKU信息超过2个商品抛弃");
            }

        } catch (Exception e) {
            throw new RuntimeException("爬取SKU信息失败", e);
        }

        logger.info("开始爬取页面script信息");
        Map<String, String> scriptsInfoMap = new HashMap<>();
        try {
            Document document = Jsoup.parse(driver.getPageSource());
            Elements scripts = document.getElementsByTag("script");

            int runScriptsCount = 0;
            for (runScriptsCount = 0; runScriptsCount < scripts.size(); runScriptsCount++) {
                String html = scripts.get(runScriptsCount).html();
                if (StringUtils.isEmpty(html.trim())) {
                    continue;
                }
                StrUtil.readLine(html, new ReadLine<String>() {
                    private boolean skuStart = false;
                    private boolean descStart = false;
                    private boolean auctionImagesStart = false;

                    @Override
                    public void nextLine(String line, int number) {
                        if (line.indexOf("Hub.config.set('sku'") != -1) {
                            skuStart = true;
                            return;
                        }

                        if (line.indexOf("Hub.config.set('desc'") != -1) {
                            descStart = true;
                            return;
                        }

                        if (line.indexOf("var g_config = {") != -1) {
                            auctionImagesStart = true;
                            return;
                        }

                        if (line.indexOf("wholeSibUrl") != -1 && skuStart) {
                            scriptsInfoMap.put("wholeSibUrl", line.substring(line.indexOf("'") + 1, line.lastIndexOf("'")));
                            logger.debug("爬取到wholeSibUrl【", scriptsInfoMap.get("wholeSibUrl"), "】");
                            return;
                        }

                        if (line.indexOf("skuMap") != -1 && skuStart) {
                            String value = line.substring(line.indexOf("{"));
                            for (String excludeSku : excludeSkuIdList) {
                                value = value.replace(";" + excludeSku, "");
                            }
                            scriptsInfoMap.put("skuMap", value);
                            logger.debug("爬取到skuMap【", scriptsInfoMap.get("skuMap"), "】");
                            return;
                        }

                        if (line.indexOf("propertyMemoMap") != -1 && skuStart) {
                            String value = line.substring(line.indexOf("{"));
                            for (String excludeSku : excludeSkuIdList) {
                                value = value.replace(";" + excludeSku, "");
                            }
                            scriptsInfoMap.put("propertyMemoMap", value);
                            logger.debug("爬取到propertyMemoMap【", scriptsInfoMap.get("propertyMemoMap"), "】");
                            skuStart = false;
                            return;
                        }

                        if (line.indexOf("apiImgInfo") != -1 && descStart) {
                            String api = line.substring(line.indexOf("'") + 1);
                            api = api.substring(0, api.lastIndexOf("'"));
                            scriptsInfoMap.put("apiImgInfo", api);
                            logger.debug("爬取到apiImgInfo【", scriptsInfoMap.get("apiImgInfo"), "】");
                            descStart = false;
                            return;
                        }

                        if (line.indexOf("auctionImages") != -1 && auctionImagesStart) {
                            scriptsInfoMap.put("auctionImages", line.substring(line.indexOf("[")));
                            logger.debug("爬取到auctionImages【", scriptsInfoMap.get("auctionImages"), "】");
                            auctionImagesStart = false;
                            return;
                        }

                    }
                });

                if (scriptsInfoMap.size() >= 5) {
                    break;
                }
            }
            logger.debug("循环script【", runScriptsCount, "】次");
        } catch (Exception e) {
            throw new RuntimeException("爬取script信息失败", e);
        }

        logger.info("设置预览图片");
        try {
            List<String> cycleImages = new ArrayList<String>();
            JSONArray auctionImagesArray = JSONArray.parseArray(scriptsInfoMap.get("auctionImages"));
            for (int i = 0; i < auctionImagesArray.size(); i++) {
                cycleImages.add("http:" + auctionImagesArray.getString(i).replace("&amp;", "&"));
            }
            goods.setCycleImages(cycleImages);
        } catch (Exception e) {
            throw new RuntimeException("爬取预览图片失败", e);
        }

        if (StringUtils.isNotEmpty(scriptsInfoMap.get("propertyMemoMap"))) {
            logger.info("爬取补充sku名称信息");
            try {
                JSONObject propertyMemoMapObject = JSONObject.parseObject(scriptsInfoMap.get("propertyMemoMap"));
                Iterator<String> propertyMemoMapObjectIt = propertyMemoMapObject.keySet().iterator();
                while (propertyMemoMapObjectIt.hasNext()) {
                    String key = propertyMemoMapObjectIt.next();
                    String text = propertyMemoMapObject.getString(key);
                    SimpleSkuEntity skuEntity = simpleSkuMap.get(key);
                    if (skuEntity != null && StringUtils.isEmpty(skuEntity.getTaobaoSkuText())) {
                        skuEntity.setTaobaoSkuText(text);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("爬取补充sku名称信息失败", e);
            }
        }

        logger.info("爬取sku组合价格信息");
        JSONObject sibJson = null;
        try {
            ((JavascriptExecutor) driver).executeScript("window.open(arguments[0]);", "https:" + scriptsInfoMap.get("wholeSibUrl").replace("&amp;", "&"));
            WebDriverUtils.switchWindow(driver, driver.getWindowHandles().size() - 1);
            String sibInfo = WebDriverUtils.waitTagname(driver, "pre").getText();
            for (String excludeSku : excludeSkuIdList) {
                sibInfo = sibInfo.replace(";" + excludeSku, "");
            }
            logger.debug("sibInfo【", sibInfo, "】");
            if (StringUtils.isEmpty(sibInfo)) {
                throw new RuntimeException("sibInfo为空");
            }
            sibJson = JSONObject.parseObject(sibInfo);
        } catch (Exception e) {
            throw new RuntimeException("爬取sku组合价格信息失败", e);
        } finally {
            WebDriverUtils.closeWindow(driver, driver.getWindowHandles().size() - 1);
        }

        logger.info("组合sku原价，活动价，库存等信息");
        try {
            List<MultipleSkuEntity> multipleSkuList = new ArrayList<MultipleSkuEntity>();
            JSONObject originalPriceJSON = sibJson.getJSONObject("data").getJSONObject("originalPrice");
            JSONObject stockInfo = sibJson.getJSONObject("data").getJSONObject("dynStock");
            JSONObject promotionPriceJSON = sibJson.getJSONObject("data").getJSONObject("promotion").getJSONObject("promoData");

            for (String mSkuId : multipleSkuIdList) {
                MultipleSkuEntity mskuEntity = new MultipleSkuEntity();
                try {
                    String crawlMSkuId = mSkuId.equals("def") ? mSkuId : ";" + mSkuId + ";";
                    String originalPrice = originalPriceJSON.getJSONObject(crawlMSkuId).getString("price");
                    String realPrice = null;
                    try {
                        realPrice = promotionPriceJSON.getJSONArray(crawlMSkuId).getJSONObject(0).getString("price");
                    } catch (Exception e) {
                        logger.warn("获取活动价失败，使用原价【", originalPrice, "】");
                        realPrice = originalPrice;
                    }
                    String stock = mSkuId.equals("def") ? stockInfo.getString("stock") : stockInfo.getJSONObject("sku").getJSONObject(crawlMSkuId).getString("stock");
                    mskuEntity.setBuyMultipleSkuId(mSkuId);
                    mskuEntity.setOriginalPrice(originalPrice);
                    mskuEntity.setRealPrice(realPrice);
                    mskuEntity.setStock(stock);
                } catch (Exception e) {
                    logger.warn("根据【", mSkuId, "】 获取sku信息");
                    mskuEntity.setBuyMultipleSkuId("none");
                    mskuEntity.setOriginalPrice(goods.getCurrentprice());
                    mskuEntity.setRealPrice(goods.getCurrentprice());
                    mskuEntity.setStock("0");
                }
                List<SimpleSkuEntity> simpleSkuList = new ArrayList<SimpleSkuEntity>();
                for (String skuId : mSkuId.split(";")) {
                    simpleSkuList.add(simpleSkuMap.get(skuId));
                }
                mskuEntity.setSkuList(simpleSkuList);
                multipleSkuList.add(mskuEntity);
            }
            goods.setSkus(multipleSkuList);
        } catch (Exception e) {
            throw new RuntimeException("组合sku原价，活动价，库存等信息失败", e);
        }

        logger.info("爬取详情图片");
        List<String> descImages = new ArrayList<String>();
        try {
            ((JavascriptExecutor) driver).executeScript("window.open(arguments[0]);", "https:" + scriptsInfoMap.get("apiImgInfo").replace("&amp;", "&"));
            WebDriverUtils.switchWindow(driver, driver.getWindowHandles().size() - 1);
            String apiImgInfoStr = WebDriverUtils.waitTagname(driver, "pre").getText();
            apiImgInfoStr = apiImgInfoStr.replace("\n", "");
            logger.debug("爬取到详情图片信息【", apiImgInfoStr.substring(apiImgInfoStr.indexOf("{"), apiImgInfoStr.lastIndexOf("}") + 1), "】");
            JSONObject apiImageInfoJSON = JSONObject.parseObject(apiImgInfoStr.substring(apiImgInfoStr.indexOf("{"), apiImgInfoStr.lastIndexOf("}") + 1));
            Iterator<String> apiImageInfoKeyIt = apiImageInfoJSON.keySet().iterator();
            while (apiImageInfoKeyIt.hasNext()) {
                String key = apiImageInfoKeyIt.next();
                if (!key.startsWith("TB") && !key.endsWith(".jpg") && !key.endsWith(".JPG")) {
                    continue;
                }
                descImages.add(URLConstant.AMM_ITEMDETAILS_IMG + key);
            }
        } catch (Exception e) {
            logger.warn(e, "爬取详情图片失败");
        } finally {
            WebDriverUtils.closeWindow(driver, driver.getWindowHandles().size() - 1);
        }

        if (descImages.isEmpty()) {
            try {
                logger.info("从页面爬取");
                List<WebElement> descImgEls = WebDriverUtils.waitXpaths(driver, XPathConstant.AMM_ITEMDETAILS_DESCIMG);
                for (WebElement descImgEl : descImgEls) {
                    descImages.add(descImgEl.getAttribute("background"));
                }
            } catch (Exception e) {
                throw new RuntimeException("爬取详情图片失败", e);
            }
        }
        goods.setDescImages(descImages);
        return goods;
    }

}
