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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.qingfox.framework.test.pdd.constant.ParamConstant;
import org.qingfox.framework.test.pdd.constant.PathConstant;
import org.qingfox.framework.test.pdd.constant.ServiceTypeConstant;
import org.qingfox.framework.test.pdd.constant.URLConstant;
import org.qingfox.framework.test.pdd.entity.ExpressEntity;
import org.qingfox.framework.test.pdd.entity.GoodsEntity;
import org.qingfox.framework.test.pdd.entity.ImageEntity;
import org.qingfox.framework.test.pdd.entity.MultipleSkuEntity;
import org.qingfox.framework.test.pdd.entity.OrderEntity;
import org.qingfox.framework.test.pdd.entity.SimpleSkuEntity;
import org.qingfox.framework.test.pdd.service.inf.ICatMatchService;
import org.qingfox.framework.test.pdd.service.inf.ISellService;
import org.qingfox.framework.test.pdd.service.listeners.ChatListener;
import org.qingfox.framework.test.pdd.utils.DataFileUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.framework.common.exceptions.ServiceException;
import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.common.tool.HttpClient;
import com.framework.common.tool.websocket.WebSocketClient;
import com.framework.common.tool.websocket.listener.WebsSocketListener;
import com.framework.common.utils.DateUtil;
import com.framework.common.utils.FileUtil;
import com.framework.common.utils.ImageUtils;
import com.framework.common.utils.PropertiesUtil;
import com.framework.common.utils.ReadLine;
import com.framework.common.utils.StrUtil;
import com.framework.common.utils.ThreadUtil;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see: @创建日期：2017年12月13日 @功能说明：
 * 
 */
public class PddService extends LoginService implements ISellService {

	private static final ILogger logger = LoggerFactory.getLogger(PddService.class);

	private HttpClient httpClient;
	private String token = null;
	private Long heartTime = 60000 * 10L;

	private WebSocketClient webSocketClient;
	private ChatListener chatListener;

	private static Map<String, ICatMatchService> catMatchServiceMap;
	private static List<ExpressEntity> expressList;
	private String[] wordsFilter;
	private String mallId;
	private String mainCatId;
	private String sellType;
	private String goodsDescAppend = "欢饮选购，详情可咨询客服，除偏远地区外全部包邮";
	private String costTemplateId = null;
	private static Map<String, Integer> skuDataMap;
	private int increase = 0;
	private Integer simplePriceUpType = 1;
	private Map<String, String> catDefaultChildMap;

	public PddService() throws Exception {
		httpClient = new HttpClient();
		if (catMatchServiceMap == null) {
			catMatchServiceMap = new HashMap<String, ICatMatchService>();
			catMatchServiceMap.put(ServiceTypeConstant.TAOBAO, new TaobaoToPddCatMatchService(PathConstant.DIR_ROOT + File.separator + ServiceTypeConstant.PDD));
		}

		if (expressList == null) {
			expressList = new ArrayList<ExpressEntity>();
			FileUtil.readLine(new File(PathConstant.DIR_ROOT + File.separator + ServiceTypeConstant.PDD + File.separator + "express.dat"), new ReadLine<Void>() {
				@Override
				public void nextLine(String line, int number) {
					JSONObject json = JSONObject.parseObject(line);
					ExpressEntity express = new ExpressEntity();
					express.setId(json.getString("shipping_id"));
					express.setName(json.getString("shipping_name"));
					expressList.add(express);
				}
			});
		}
	}

	/**
	 * 刷新验证码 {@inheritDoc}
	 * 
	 * @see org.qingfox.framework.test.pdd.service.inf.ISellService#refreshCode()
	 * @author Administrator 2018年1月5日 Administrator
	 */
	public void refreshCode() throws Exception {
		logger.info("开始读取验证码");
		JSONObject requestJSON = new JSONObject();
		JSONObject responseJSON = null;
		responseJSON = this.postResult(URLConstant.PDD_LOGIN_CODE, null);
		if (responseJSON == null || !responseJSON.getBooleanValue("result")) {
			throw new ServiceException("请求验证码失败【", (responseJSON == null ? "" : responseJSON.getString("error_msg")), "】");
		}

		token = responseJSON.getJSONObject("data").getString("token");
		logger.debug("token【", token, "】");
		if (StringUtils.isEmpty(token)) {
			throw new ServiceException("获取到token为空");
		}
		requestJSON.put("token", token);
		String imageCode = responseJSON.getJSONObject("data").getString("image").replace("data:image/png;base64,", "");

		File codeFile = new File(PathConstant.DIR_ROOT + File.separator + PathConstant.DIR_TEMP + File.separator + "pdd_code_" + DateUtil.getNowDate(DateUtil.PATTERN_YYYYMMDDHHMMSS) + ".jpg");
		if (!codeFile.getParentFile().exists()) {
			codeFile.getParentFile().mkdirs();
		}
		ImageUtils.setBase64Image(imageCode, codeFile.getPath());
		onCodeChange(codeFile);
	}

	/**
	 * 登录 {@inheritDoc}
	 * 
	 * @see org.qingfox.framework.test.pdd.service.inf.ISellService#login(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 * @author Administrator 2018年1月5日 Administrator
	 */
	public void login(String username, String password, String code) throws Exception {
		logger.info("开始登陆操作");
		JSONObject requestJSON = null;
		JSONObject responseJSON = null;

		requestJSON = new JSONObject();
		logger.debug("username【", username, "】");
		if (StringUtils.isEmpty(username)) {
			throw new ServiceException("用户名为空");
		}
		requestJSON.put("username", username);

		logger.debug("password【", password, "】");
		if (StringUtils.isEmpty(password)) {
			throw new ServiceException("密码为空");
		}
		requestJSON.put("password", password);

		logger.debug("code【", code, "】");
		if (StringUtils.isEmpty(code)) {
			throw new ServiceException("验证码为空");
		}
		requestJSON.put("authCode", code);

		logger.debug("token【", token, "】");
		if (StringUtils.isEmpty(token)) {
			throw new ServiceException("token为空");
		}
		requestJSON.put("token", token);

		logger.info("请求登录");
		responseJSON = this.postResult(URLConstant.PDD_LOGIN, requestJSON);
		if (responseJSON == null || !responseJSON.getBooleanValue("authResult")) {
			throw new ServiceException("登录失败");
		}

		this.mallId = responseJSON.getJSONObject("userInfo").getString("mallId");
		logger.debug("mallId【", mallId, "】");
		if (StringUtils.isEmpty(mallId)) {
			throw new ServiceException("商店ID为空");
		}

		logger.info("开始初始化sku数据");
		skuDataMap = new HashMap<String, Integer>();
		responseJSON = this.postResult(URLConstant.PDD_SKU_LIST, new JSONObject());
		if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
			throw new ServiceException("请求sku信息失败【", (responseJSON == null ? "" : responseJSON.getString("error_msg")), "】");
		}
		JSONArray skuArray = responseJSON.getJSONArray("result");
		requestJSON = new JSONObject();
		for (int i = 0; i < skuArray.size(); i++) {
			String name = skuArray.getJSONObject(i).getString("value");
			requestJSON.put("parent_id", 0);
			requestJSON.put("name", name);
			responseJSON = this.postResult(URLConstant.PDD_SKU_DESC, requestJSON);
			skuDataMap.put(name, responseJSON.getInteger("result"));
		}

		logger.info("开始初始化运费模板ID");
		requestJSON = new JSONObject();
		requestJSON.put("mallId", mallId);
		requestJSON.put("pageNo", 1);
		requestJSON.put("pageSize", 1000);
		responseJSON = this.postResult(URLConstant.PDD_GOODS_COSTTEMPLATE, requestJSON);
		if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
			throw new ServiceException("初始化运费模板ID失败【", (responseJSON == null ? "" : responseJSON.getString("error_msg")), "】");
		}
		this.costTemplateId = responseJSON.getJSONObject("result").getJSONArray("list").getJSONObject(0).getString("costTemplateId");
		logger.debug("运费模板ID【", costTemplateId, "】");
		if (StringUtils.isEmpty(this.costTemplateId)) {
			throw new ServiceException("运费模板ID为空");
		}

		logger.info("初始化主营分类ID");
		responseJSON = this.postResult(URLConstant.PDD_MAINCAT, null);
		if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
			throw new ServiceException("初始化主营分类ID失败【", (responseJSON == null ? "" : responseJSON.getString("error_msg")), "】");
		}
		this.mainCatId = responseJSON.getJSONArray("result").getJSONObject(0).getString("id");
		logger.debug("主营分类ID【", mainCatId, "】");
		if (StringUtils.isEmpty(this.mainCatId)) {
			throw new ServiceException("主营分类ID为空");
		}
		this.sellType = PropertiesUtil.loadProperties(new File(PathConstant.DIR_ROOT + File.separator + ServiceTypeConstant.PDD + File.separator + "selltype_cat.properties")).getProperty(this.mainCatId);

		logger.info("获取默认父子对应信息");
		List<JSONObject> catDefaultChildList = DataFileUtils.readJSONFile(new File(PathConstant.DIR_ROOT + File.separator + ServiceTypeConstant.PDD + File.separator + "cat_default_child.dat"));
		catDefaultChildMap = new HashMap<String, String>();
		for (JSONObject catDefaultChild : catDefaultChildList) {
			catDefaultChildMap.put(catDefaultChild.getString("mainCat"), catDefaultChild.getString("childCat"));
		}

		logger.info("读取过滤词数据");
		File wordsFilterFile = new File(PathConstant.DIR_ROOT + File.separator + ServiceTypeConstant.PDD + File.separator + "words_filter.dat");
		if (!wordsFilterFile.exists()) {
			wordsFilter = FileUtils.readFileToString(wordsFilterFile).split(",");
		}
		setUsername(username);
		setPassword(password);
		setLogin(true);
		onLogin();
	}

	/**
	 * post请求封装 .
	 * 
	 * @param url
	 * @param requestJSON
	 * @return
	 * @throws IOException
	 * @author Administrator 2017年12月25日 Administrator
	 */
	public JSONObject postResult(String url, JSONObject requestJSON) throws IOException {
		JSONObject responseJSON = new JSONObject();
		CloseableHttpResponse response = null;
		try {
			if (requestJSON == null) {
				response = httpClient.get(url);
			} else {
				logger.debug("POST请求信息【", requestJSON.toJSONString(), "】");
				response = httpClient.post(url, null, requestJSON.toJSONString());
			}
			responseJSON = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
			logger.debug("POST返回信息【", responseJSON == null ? "" : responseJSON.toJSONString(), "】");
			return responseJSON;
		} catch (IOException e) {
			throw e;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error(e, "关闭response失败");
				}
			}
		}
	}

	/**
	 * 创建商品 .
	 * 
	 * @param goods
	 * @throws Exception
	 * @author Administrator 2018年1月5日 Administrator
	 */
	public void createGoods(GoodsEntity goods) throws Exception {
		checkLogin();
		logger.info("开始创建商品");
		Integer simplePriceUpValue = goods.getCommission() <= 5 ? 10 : 5;
		JSONObject responseJSON = null;
		JSONObject goodsJSON = new JSONObject();
		File goodsTemplateFile = new File(PathConstant.DIR_ROOT + File.separator + ServiceTypeConstant.PDD + File.separator + "goods_template_" + this.mainCatId + ".dat");

		if (!goodsTemplateFile.exists()) {
			goodsTemplateFile = new File(PathConstant.DIR_ROOT + File.separator + ServiceTypeConstant.PDD + File.separator + "goods_template_default.dat");
		}

		goodsJSON = JSONObject.parseObject(FileUtils.readFileToString(goodsTemplateFile));
		goodsJSON.put("weight", "0");

		logger.info("开始请求商品ID以及商品SN");
		responseJSON = this.postResult(URLConstant.PDD_GOODS_CREATE, new JSONObject());
		if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
			throw new ServiceException(responseJSON.getString("error_code"), "获取商品ID以及商品SN失败【", (responseJSON == null ? "" : responseJSON.getString("error_msg")), "】");
		}

		// 验证商品ID
		String goodsCommitId = responseJSON.getJSONObject("result").getString("goods_commit_id");
		logger.debug("goodsCommitId【", goodsCommitId, "】");
		if (StringUtils.isEmpty(goodsCommitId)) {
			throw new ServiceException("商品ID为空");
		}
		goodsJSON.put("goods_commit_id", goodsCommitId);

		// 验证商品SN
		String goodsSn = responseJSON.getJSONObject("result").getString("goods_sn");
		logger.debug("goodsSn【", goodsSn, "】");
		if (StringUtils.isEmpty(goodsSn)) {
			throw new ServiceException("商品SN为空");
		}
		goods.setSellSn(goodsSn);

		// 验证PDD商品分类ID
		String buyCatId = goods.getBuyCatId();
		logger.debug("buyCatId【", buyCatId, "】");
		String sellCatId = catMatchServiceMap.get(goods.getBuyType()).getCatId(this.getMainCatId(), buyCatId, goods.getName());
		goods.setSellCatId(sellCatId);
		if (StringUtils.isEmpty(goods.getSellCatId())) {
			goods.setSellCatId(catDefaultChildMap.get(mainCatId));
			throw new ServiceException("商品分类ID为空");
		}
		if (StringUtils.isEmpty(goods.getSellCatId())) {
			throw new ServiceException("商品分类ID为空");
		}
		logger.debug("sellCatId【", goods.getSellCatId(), "】");
		goodsJSON.put("cat_id", goods.getSellCatId());

		// 验证PDD商品名称
		logger.debug("name【", goods.getName(), "】");
		if (StringUtils.isEmpty(goods.getName())) {
			throw new ServiceException("商品名称为空");

		}

		String goodsName = goods.getName();
		logger.debug("goodsName【", goodsName, "】");
		if (this.wordsFilter != null) {
			for (String word : this.wordsFilter) {
				goodsName = goodsName.replace(word, "");
			}
		}
		goodsJSON.put("goods_name", StrUtil.shorten(goodsName, 60));
		goodsJSON.put("tiny_name", StrUtil.shorten(goodsName, 20));

		// 保存商品
		int tryCount = 0;
		do {
			logger.info("开始保存商品");
			responseJSON = this.postResult(URLConstant.PDD_GOODS_UPDATE, goodsJSON);
			if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
				logger.warn(responseJSON.getString("error_msg"));
				ThreadUtil.sleep(1000);
			} else {
				break;
			}
			tryCount++;
		} while (tryCount < 3);

		if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
			throw new ServiceException("保存商品信息失败【", (responseJSON == null ? "" : responseJSON.getString("error_msg")), "】");
		}

		// 验证市场价
		if (StringUtils.isEmpty(goods.getCurrentprice())) {
			throw new ServiceException("商品市场价为空");
		}

		Float marketPrice = Float.parseFloat(goods.getCurrentprice()) * 100 + increase * 100;
		logger.debug("marketPrice【", marketPrice, "】");
		goodsJSON.put("market_price", marketPrice.intValue());

		// 设置商品描述
		String goodsDesc = StrUtil.shorten(StringUtils.isEmpty(goods.getDesc()) ? goodsDescAppend : goods.getDesc() + goodsDescAppend, 300);
		logger.debug("goodsDesc【", goodsDesc, "】");
		if (this.wordsFilter != null) {
			for (String word : this.wordsFilter) {
				goodsDesc = goodsDesc.replace(word, "");
			}
		}

		goodsJSON.put("share_desc", goodsDesc);
		goodsJSON.put("goods_desc", goodsDesc);

		// 设置商品amm推广地址
		if (StringUtils.isEmpty(goods.getBuyUrl())) {
			throw new ServiceException("购买地址为空");
		}
		goodsJSON.put("out_goods_sn", "pdd:" + goods.getBuyId() + ":" + goods.getBuyUrl());

		// 设置邮费模板
		goodsJSON.put("cost_template_id", costTemplateId);

		// 验证商品图片
		List<String> cycleImages = goods.getCycleImages();
		if (cycleImages == null || cycleImages.isEmpty()) {
			throw new ServiceException("商品图片为空");
		}

		// 主图上传
		logger.info("开始上传主图高清图");
		ImageEntity hdImage = uploadImage(cycleImages.get(0), 400, 400, 1024 * 1024, null, null);
		goodsJSON.put("hd_thumb_url", hdImage.getUrl());

		// 主图上传
		logger.info("开始上传主图");
		ImageEntity thumbImage = uploadImage(cycleImages.get(0), 200, 200, 1024 * 1024, null, null);
		goodsJSON.put("thumb_url", thumbImage.getUrl());

		JSONArray galleryArray = new JSONArray();

		// 上传轮播图
		String defaultImage = null;
		logger.info("开始上传轮播图");
		boolean hasCycleImage = false;
		int cycleImageCount = 0;
		for (String cycleImage : cycleImages) {
			if (cycleImageCount >= 10) {
				break;
			}
			try {
				JSONObject galleryObject = new JSONObject();
				ImageEntity uploadImage = uploadImage(cycleImage, 480, 480, 1024 * 1024, null, null);
				galleryObject.put("url", uploadImage.getUrl());
				if (StringUtils.isEmpty(defaultImage)) {
					defaultImage = uploadImage.getUrl();
				}
				galleryObject.put("width", 480);
				galleryObject.put("height", 480);
				galleryObject.put("type", "1");
				galleryArray.add(galleryObject);
				hasCycleImage = true;
				cycleImageCount++;
			} catch (Exception e) {
				logger.warn(e, "上传轮播图【", cycleImage, "】失败");
			}
		}
		if (!hasCycleImage) {
			throw new ServiceException("轮播图为空");
		}

		// 上传详情图
		logger.info("开始上传详情图片");
		boolean hasDescImage = false;
		int dcsImageCount = 0;
		for (String descImage : goods.getDescImages()) {
			if (dcsImageCount >= 20) {
				break;
			}
			try {
				JSONObject descObject = new JSONObject();
				ImageEntity uploadImage = uploadImage(descImage, null, null, 1024 * 1024, "480:1200", "0:1500");// 480x480-1mb
				descObject.put("url", uploadImage.getUrl());
				descObject.put("width", uploadImage.getWidth());
				descObject.put("height", uploadImage.getHeight());
				descObject.put("type", "2");
				galleryArray.add(descObject);
				hasDescImage = true;
				dcsImageCount++;
			} catch (Exception e) {
				logger.warn(e, "上传详情图片【", descImage, "】失败");
			}
		}
		if (!hasDescImage) {
			throw new ServiceException("详情图为空");
		}

		goodsJSON.put("gallery", galleryArray);

		// 设置sku信息
		logger.info("开始设置sku信息");
		List<MultipleSkuEntity> skuList = goods.getSkus();
		if (skuList == null || skuList.isEmpty()) {
			throw new ServiceException("sku列表为空");
		}

		JSONArray skusArray = new JSONArray();
		for (MultipleSkuEntity multipleSku : skuList) {
			String taobaoMultipeSkuId = multipleSku.getBuyMultipleSkuId();
			JSONObject skuJSON = new JSONObject();
			skuJSON.put("is_onsale", taobaoMultipeSkuId.equals("none") || multipleSku.getStock().equals("0") ? 0 : 1);
			skuJSON.put("limit_quantity", 999);
			skuJSON.put("quantity_delta", multipleSku.getStock());
			skuJSON.put("id", 0);
			skuJSON.put("weight", 0);

			int realPrice = taobaoMultipeSkuId.equals("none") ? marketPrice.intValue() - 200 : ((Float) (Float.parseFloat(multipleSku.getRealPrice()) * 100)).intValue();
			realPrice += increase * 100;
			skuJSON.put("multi_price", realPrice);

			int skuPrice = taobaoMultipeSkuId.equals("none") ? marketPrice.intValue() - 100 : new Float(this.simplePriceUpType == 0 ? (realPrice + realPrice * ((float) simplePriceUpValue / 100)) : realPrice + (float) simplePriceUpValue).intValue();
			skuPrice += increase * 100;
			skuPrice = skuPrice - realPrice < 100 ? skuPrice + 100 : skuPrice;
			skuJSON.put("price", skuPrice);
			if (skuPrice >= marketPrice) {
				goodsJSON.put("market_price", skuPrice + skuPrice * 0.5);
			}
			skuJSON.put("out_sku_sn", taobaoMultipeSkuId);

			String skuImage = null;
			if (taobaoMultipeSkuId.equals("def")) {
				skuJSON.put("spec", "");
			} else {
				JSONArray specArray = new JSONArray();
				for (SimpleSkuEntity simplSku : multipleSku.getSkuList()) {
					JSONObject specJSON = new JSONObject();
					if (StringUtils.isNotEmpty(simplSku.getTaobaoSkuImgUrl())) {
						skuImage = simplSku.getTaobaoSkuImgUrl();
					}
					setSkuParent(specJSON, simplSku.getTaobaoSkuParentText());
					Integer specId = getSkuSpec(specJSON.getString("parent_id"), simplSku.getTaobaoSkuText());
					specJSON.put("spec_name", simplSku.getTaobaoSkuText());
					specJSON.put("spec_id", specId);
					specArray.add(specJSON);

				}
				skuJSON.put("spec", specArray);
			}

			if (StringUtils.isNotEmpty(skuImage)) {
				try {
					ImageEntity uploadImage = uploadImage(skuImage, 480, 480, 1024 * 1024, null, null);
					skuJSON.put("thumb_url", uploadImage.getUrl());
				} catch (Exception e) {
					logger.warn(e, "sku图片上传失败");
					skuJSON.put("thumb_url", defaultImage);
				}
			} else {
				skuJSON.put("thumb_url", defaultImage);
			}

			skusArray.add(skuJSON);
		}
		goodsJSON.put("skus", skusArray);

		// 保存商品
		logger.info("开始保存商品");
		responseJSON = this.postResult(URLConstant.PDD_GOODS_UPDATE, goodsJSON);
		if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
			throw new ServiceException("保存商品信息失败【", (responseJSON == null ? "" : responseJSON.getString("error_msg")), "】");
		}

		// 发布商品
		logger.info("开始发布商品");
		JSONObject requestJSON = new JSONObject();
		requestJSON.put("goods_commit_id", goodsCommitId);
		responseJSON = this.postResult(URLConstant.PDD_GOODS_CREATE_SUBMIT, requestJSON);
		if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
			throw new ServiceException("发布商品失败【", (responseJSON == null ? "" : responseJSON.getString("error_msg")), "】");
		}
	}

	/**
	 * 设置sku的parent .
	 * 
	 * @param specJSON
	 * @param parentText
	 * @author Administrator 2017年12月19日 Administrator
	 */
	private void setSkuParent(JSONObject specJSON, String parentText) {
		String parentName = null;
		Integer parentId = skuDataMap.get(parentText);
		if (parentId == null) {
			Iterator<String> it = skuDataMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (parentText.indexOf(key) != -1) {
					parentName = key;
					parentId = skuDataMap.get(parentName);
					break;
				}

				if (key.indexOf(parentText) != -1) {
					parentName = key;
					parentId = skuDataMap.get(parentName);
					break;
				}
			}
		} else {
			parentName = parentText;
		}

		if (parentName == null) {
			parentName = "型号";
			parentId = skuDataMap.get(parentName);
		}

		specJSON.put("parent_name", parentName);
		specJSON.put("parent_id", parentId);

	}

	/**
	 * 上传图片 .
	 * 
	 * @param url
	 * @param width
	 * @param height
	 * @param length
	 * @return
	 * @author Administrator 2017年12月25日 Administrator
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private ImageEntity uploadImage(String url, Integer width, Integer height, Integer length, String limitWidth, String limitHeight) throws Exception {
		logger.info("开始上传图片");
		File imageFile = new File(PathConstant.DIR_ROOT + File.separator + PathConstant.DIR_TEMP + File.separator + "pdd_image_" + System.currentTimeMillis());
		logger.info("下载图片【", url, "】到本地【", imageFile.getPath(), "】");
		ImageUtils.download(url, imageFile);

		try {
			BufferedImage bufferImage = ImageIO.read(imageFile);
			int imageWidth = bufferImage.getWidth();
			int imageHeight = bufferImage.getHeight();
			int cutWidth = imageWidth;
			int cutHeight = imageHeight;
			boolean cutImage = false;
			if (width != null && imageWidth != width) {
				cutWidth = width;
				cutImage = true;
			}

			if (height != null && imageHeight != height) {
				cutHeight = height;
				cutImage = true;
			}

			if (StringUtils.isNotEmpty(limitWidth)) {
				String[] limits = limitWidth.split(":");
				if (cutWidth < Integer.parseInt(limits[0])) {
					cutWidth = Integer.parseInt(limits[0]);
					cutImage = true;
				}

				if (cutWidth > Integer.parseInt(limits[1])) {
					cutWidth = Integer.parseInt(limits[1]);
					cutImage = true;
				}
			}

			if (StringUtils.isNotEmpty(limitHeight)) {
				String[] limits = limitWidth.split(":");
				if (cutHeight < Integer.parseInt(limits[0])) {
					cutHeight = Integer.parseInt(limits[0]);
					cutImage = true;
				}

				if (cutHeight > Integer.parseInt(limits[1])) {
					cutHeight = Integer.parseInt(limits[1]);
					cutImage = true;
				}
			}

			if (cutImage) {
				bufferImage = ImageUtils.changeImageSize(bufferImage, cutWidth, cutHeight);
				ImageIO.write(bufferImage, "jpg", new FileOutputStream(imageFile));
			}

			if (length != null && imageFile.length() > length) {
				imageFile = ImageUtils.commpressPicForScale(imageFile, length, 0.8);
			}

			logger.info("开始请求上传CODE");
			JSONObject responseJSON = null;
			responseJSON = this.postResult(URLConstant.PDD_GOODS_CREATE_IMAGE_UPLOAD, new JSONObject());
			if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
				throw new ServiceException("请求上传CODE失败【", (responseJSON == null ? "" : responseJSON.getString("error_msg")), "】");
			}
			String signature = responseJSON.getJSONObject("result").getString("signature");
			String uploadUrl = responseJSON.getJSONObject("result").getString("url");

			logger.info("将图片转化为BASE64字符串");
			String imageBase64Str = null;
			try {
				imageBase64Str = ImageUtils.getImageBase64(imageFile);
			} catch (Exception e) {
				throw new ServiceException("将图片转化为BASE64字符串失败");
			}

			JSONObject requestJSON = new JSONObject();
			requestJSON.put("upload_sign", signature);
			requestJSON.put("image", "data:image/jpeg;base64," + imageBase64Str);
			responseJSON = this.postResult(uploadUrl, requestJSON);
			logger.debug("返回信息【", responseJSON.toJSONString(), "】");
			// ThreadUtil.sleep(1000);
			String resultUrl = responseJSON.getString("url");
			if (StringUtils.isEmpty(resultUrl)) {
				throw new ServiceException("图片上传返回地址为空");
			}
			ImageEntity imageEntity = new ImageEntity();
			imageEntity.setUrl(resultUrl);
			imageEntity.setWidth(responseJSON.getInteger("width"));
			imageEntity.setHeight(responseJSON.getInteger("height"));
			return imageEntity;
		} catch (Exception e) {
			throw e;
		} finally {
			FileUtils.deleteQuietly(imageFile);
		}
	}

	/**
	 * 获取商品列表 .
	 * 
	 * @return
	 * @throws Exception
	 * @author Administrator 2018年1月5日 Administrator
	 */
	public List<GoodsEntity> getGoodsList() throws Exception {
		checkLogin();
		logger.info("开始获取商品列表");
		List<GoodsEntity> list = new ArrayList<GoodsEntity>();
		Integer total = null;
		int count = 0;
		int page = 1;
		JSONObject responseJson = null;
		do {
			responseJson = this.postResult(URLConstant.PDD_GOODS_ONSELL_LIST.replace(ParamConstant.PDD_MALLID, mallId) + page, null);
			if (responseJson == null || !responseJson.getBooleanValue("result")) {
				throw new ServiceException("获取商品列表失败【", (responseJson == null ? "" : responseJson.getString("error_msg")), "】");
			}
			total = responseJson.getJSONObject("data").getInteger("total");
			logger.debug("获取到total数量为【", total, "】");

			JSONArray goodsArray = responseJson.getJSONObject("data").getJSONArray("goodsList");
			for (int i = 0; i < goodsArray.size(); i++) {
				count++;
				try {
					String goodsSn = goodsArray.getJSONObject(i).getString("goods_sn");
					String goodsId = goodsArray.getJSONObject(i).getString("id");
					GoodsEntity goods = getGoods(goodsSn);
					goods.setSellId(goodsId);
					list.add(goods);
				} catch (Exception e) {
					logger.warn(e, "获取商品详情失败");
					continue;
				}
			}
			page++;
		} while (total != null && count < total);
		return list;
	}

	/**
	 * 获取SKU信息 .
	 * 
	 * @param parentId
	 * @param specName
	 * @return
	 * @author Administrator 2017年12月25日 Administrator
	 * @throws IOException
	 */
	public Integer getSkuSpec(String parentId, String specName) throws Exception {
		checkLogin();
		logger.info("开始获取新建sku信息");
		JSONObject requestJSON = new JSONObject();
		requestJSON.put("parent_id", parentId);
		requestJSON.put("name", specName);
		JSONObject responseJSON;
		try {
			responseJSON = this.postResult(URLConstant.PDD_SKU_DESC, requestJSON);
		} catch (IOException e) {
			throw e;
		}

		if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
			throw new ServiceException("新建sku信息失败【", (responseJSON == null ? "" : responseJSON.getString("error_msg")), "】");
		}

		Integer skuId = responseJSON.getInteger("result");
		if (skuId == null) {
			throw new ServiceException("新建skuId为空");
		}

		return skuId;
	}

	/**
	 * 获取订单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OrderEntity> getOrderList() throws Exception {
		checkLogin();
		logger.info("开始获取未发货订单信息");
		List<OrderEntity> list = new ArrayList<OrderEntity>();
		Calendar cl = Calendar.getInstance();
		JSONObject requestJSON = new JSONObject();
		requestJSON.put("afterSaleType", "1");
		requestJSON.put("isLucky", -1);
		requestJSON.put("orderType", "1");
		requestJSON.put("pageSize", "30");
		requestJSON.put("confirmEndTime", StringUtils.substring(cl.getTimeInMillis() + "", 0, 10));
		cl.add(Calendar.MONTH, -1);
		requestJSON.put("confirmStartTime", StringUtils.substring(cl.getTimeInMillis() + "", 0, 10));

		Integer total = null;
		Integer pageNumber = 1;

		do {
			JSONObject responseJSON = this.postResult(URLConstant.PDD_ORDER_UNGROUP + pageNumber, null);
			if (responseJSON == null) {
				throw new ServiceException("获取订单失败");
			}

			if (total == null) {
				total = responseJSON.getInteger("total");
			}

			JSONArray pageItems = responseJSON.getJSONArray("orders");
			for (int i = 0; i < pageItems.size(); i++) {
				OrderEntity order = new OrderEntity();
				order.setSellId(getId());
				order.setSellOrderId(pageItems.getJSONObject(i).getString("orderSn"));
				order.setSellAmount(pageItems.getJSONObject(i).getString("orderAmount"));
				order.setSellGoodsSpec(pageItems.getJSONObject(i).getJSONObject("orderGoodsList").getString("spec"));
				order.setSellOrderState(OrderEntity.SELLORDERSTATE_UNGROUP);
				order.setSellGoodsId(pageItems.getJSONObject(i).getJSONObject("orderGoodsList").getString("goodsId"));
				String goodsSn = getGoodsSn(order.getSellGoodsId());
				logger.debug("goodsSn【", goodsSn, "】");
				if (StringUtils.isEmpty(goodsSn)) {
					throw new ServiceException("goodsSn为空");
				}
				GoodsEntity goods = getGoods(goodsSn);
				logger.debug("buyUrl【", goods.getBuyUrl(), "】");
				if (StringUtils.isEmpty(goods.getBuyUrl())) {
					throw new ServiceException("buyUrl为空");
				}
				order.setBuyUrl(goods.getBuyUrl());
				list.add(order);
				total--;
			}
			pageNumber++;
		} while (total > 0);

		total = null;
		pageNumber = 1;
		do {
			requestJSON.put("pageNumber", pageNumber);
			JSONObject responseJSON = this.postResult(URLConstant.PDD_ORDER_LIST, requestJSON);
			if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
				throw new ServiceException("获取订单失败【", (responseJSON == null ? "" : responseJSON.getString("error_msg")), "】");
			}

			if (total == null) {
				total = responseJSON.getJSONObject("result").getInteger("totalItemNum");
			}

			JSONArray pageItems = responseJSON.getJSONObject("result").getJSONArray("pageItems");
			for (int i = 0; i < pageItems.size(); i++) {
				OrderEntity order = getOrder(pageItems.getJSONObject(i).getString("order_sn"), pageItems.getJSONObject(i).getString("sku_id"));
				order.setSellOrderState(OrderEntity.SELLORDERSTATE_UNDONE);
				list.add(order);
				total--;
			}
			pageNumber++;
		} while (total > 0);

		return list;
	}

	/**
	 * 获取订单详情 .
	 * 
	 * @param orderSn
	 * @param skuId
	 * @return
	 * @throws Exception
	 * @author Administrator 2018年1月5日 Administrator
	 */
	private OrderEntity getOrder(String orderSn, String skuId) throws Exception {
		// if (!login) {
		// throw new ServiceException("服务未登录");
		// }
		logger.info("获取订单详情");
		OrderEntity order = new OrderEntity();
		order.setSellType(ServiceTypeConstant.PDD);

		// orderSn
		logger.debug("orderSn【", orderSn, "】");
		if (StringUtils.isEmpty(orderSn)) {
			throw new ServiceException("orderSn为空");
		}
		order.setSellOrderId(orderSn);

		// skuId
		logger.debug("skuId【", skuId, "】");
		if (StringUtils.isEmpty(skuId)) {
			throw new ServiceException("skuId为空");
		}
		order.setSellSkuId(skuId);

		JSONObject requestJson = null;
		JSONObject responseJson = null;

		requestJson = new JSONObject();
		requestJson.put("orderSn", orderSn);
		responseJson = this.postResult(URLConstant.PDD_ORDER_DETAIL, requestJson);

		if (responseJson == null || !responseJson.getBooleanValue("success")) {
			throw new ServiceException("获取订单详情失败【", (responseJson == null ? "" : responseJson.getString("error_msg")), "】");
		}
		JSONObject orderJson = responseJson.getJSONObject("result");

		// orderTime
		String orderTime = orderJson.getString("order_time");
		logger.debug("orderTime【", orderTime, "】");
		if (StringUtils.isEmpty(orderTime)) {
			throw new ServiceException("orderTime为空");
		}
		order.setSellOrderTime(orderTime);

		// goodsId
		String goodsId = orderJson.getString("goods_id");
		logger.debug("goodsId【", goodsId, "】");
		if (StringUtils.isEmpty(goodsId)) {
			throw new ServiceException("goodsId为空");
		}
		order.setSellGoodsId(goodsId);

		// goodsNumber
		String goodsNumber = orderJson.getString("goods_number");
		logger.debug("goodsNumber【", goodsNumber, "】");
		if (StringUtils.isEmpty(goodsNumber)) {
			throw new ServiceException("goodsNumber为空");
		}
		order.setSellGoodsNumber(goodsNumber);

		// provinceId
		String provinceId = orderJson.getString("province_id");
		logger.debug("provinceId【", provinceId, "】");
		if (StringUtils.isEmpty(provinceId)) {
			throw new ServiceException("provinceId为空");
		}
		order.setSellProvinceId(provinceId);

		// provinceName
		String provinceName = orderJson.getString("province_name");
		logger.debug("provinceName【", provinceName, "】");
		if (StringUtils.isEmpty(provinceName)) {
			throw new ServiceException("provinceName为空");
		}
		order.setSellProvinceName(provinceName);

		// cityId
		String cityId = orderJson.getString("city_id");
		logger.debug("cityId【", cityId, "】");
		if (StringUtils.isEmpty(cityId)) {
			throw new ServiceException("cityId为空");
		}
		order.setSellCityId(cityId);

		// cityName
		String cityName = orderJson.getString("city_name");
		logger.debug("cityName【", cityName, "】");
		if (StringUtils.isEmpty(cityName)) {
			throw new ServiceException("cityName为空");
		}
		order.setSellCityName(cityName);

		// districtId
		String districtId = orderJson.getString("district_id");
		logger.debug("districtId【", districtId, "】");
		if (StringUtils.isEmpty(districtId)) {
			throw new ServiceException("districtId为空");
		}
		order.setSellDistrictId(districtId);

		// districtName
		String districtName = orderJson.getString("district_name");
		logger.debug("districtName【", districtName, "】");
		if (StringUtils.isEmpty(districtName)) {
			throw new ServiceException("districtName为空");
		}
		order.setSellDistrictName(districtName);

		// address
		String shippingAddress = orderJson.getString("shipping_address");
		logger.debug("shippingAddress【", shippingAddress, "】");
		if (StringUtils.isEmpty(shippingAddress)) {
			throw new ServiceException("shippingAddress为空");
		}
		order.setSellAddress(shippingAddress);

		// amount
		String amount = orderJson.getString("order_amount");
		logger.debug("amount【", amount, "】");
		if (StringUtils.isEmpty(amount)) {
			throw new ServiceException("amount为空");
		}
		order.setSellAmount(amount);

		// receive_name
		String receiveName = orderJson.getString("receive_name");
		logger.debug("receiveName【", receiveName, "】");
		if (StringUtils.isEmpty(receiveName)) {
			throw new ServiceException("receiveName为空");
		}
		order.setSellReceiveName(receiveName);

		// receive_mobile
		logger.info("获取订单手机号");
		responseJson = this.postResult(URLConstant.PDD_ORDER_MOBILE, requestJson);
		if (responseJson == null || !responseJson.getBooleanValue("success")) {
			throw new ServiceException("查询手机号码失败【", (responseJson == null ? "" : responseJson.getString("error_msg")), "】");
		}
		String receiveMobile = responseJson.getJSONObject("result").getString("mobile");
		logger.debug("receiveMobile【", receiveMobile, "】");
		if (StringUtils.isEmpty(receiveMobile)) {
			throw new ServiceException("查询手机号码为空");
		}
		order.setSellReceiveMobile(receiveMobile);

		// goodsSn
		String goodsSn = getGoodsSn(goodsId);
		logger.debug("goodsSn【", goodsSn, "】");
		if (StringUtils.isEmpty(goodsSn)) {
			throw new ServiceException("goodsSn为空");
		}

		GoodsEntity goods = getGoods(goodsSn);

		logger.debug("buyUrl【", goods.getBuyUrl(), "】");
		if (StringUtils.isEmpty(goods.getBuyUrl())) {
			throw new ServiceException("buyUrl为空");
		}
		order.setBuyUrl(goods.getBuyUrl());

		List<MultipleSkuEntity> skusList = goods.getSkus();
		for (MultipleSkuEntity sku : skusList) {
			if (sku.getSellMultipleSkuId().equals(order.getSellSkuId())) {
				order.setBuySkuId(sku.getBuyMultipleSkuId());
			}
		}

		logger.debug("buySkuId【", order.getBuySkuId(), "】");
		if (StringUtils.isEmpty(order.getBuySkuId())) {
			throw new ServiceException("buySkuId为空");
		}

		return order;

	}

	/**
	 * 获取商品详情 .
	 * 
	 * @param goodsSn
	 * @return
	 * @throws Exception
	 * @author Administrator 2018年1月1日 Administrator
	 */
	private GoodsEntity getGoods(String goodsSn) throws Exception {
		logger.info("获取商品详情");
		GoodsEntity goods = new GoodsEntity();
		goods.setSellSn(goodsSn);
		JSONObject requestJson = new JSONObject();
		requestJson.put("goods_sn", goodsSn);
		JSONObject responseJson = this.postResult(URLConstant.PDD_GOODS_DETAIL, requestJson);
		if (responseJson == null || !responseJson.getBooleanValue("success")) {
			throw new ServiceException("获取商品详情失败【", (responseJson == null ? "" : responseJson.getString("error_msg")), "】");
		}
		String outGoodsSn = responseJson.getJSONObject("result").getJSONObject("commit_extension").getString("out_goods_sn");
		logger.debug("outGoodsSn【", outGoodsSn, "】");
		if (StringUtils.isEmpty(outGoodsSn)) {
			throw new ServiceException("outGoodsSn为空");
		}

		outGoodsSn = outGoodsSn.replace("https://s.click.taobao.com/", "");
		String[] outGoodsSns = outGoodsSn.split(":");
		if (outGoodsSns.length == 0 || outGoodsSns.length != 3 || !outGoodsSns[0].equals("pdd")) {
			throw new ServiceException("外部SN格式不匹配");
		}
		logger.debug("buyId【", outGoodsSns[1], "】");
		goods.setBuyId(outGoodsSns[1]);

		logger.debug("buyUrl【", outGoodsSns[2], "】");
		goods.setBuyUrl(outGoodsSns[2]);

		List<MultipleSkuEntity> skusList = new ArrayList<MultipleSkuEntity>();
		JSONArray skusArray = responseJson.getJSONObject("result").getJSONArray("skus");
		for (int i = 0; i < skusArray.size(); i++) {
			JSONObject skuObject = skusArray.getJSONObject(i);
			MultipleSkuEntity skuEntity = new MultipleSkuEntity();
			skuEntity.setSellMultipleSkuId(skuObject.getString("sku_id"));
			skuEntity.setBuyMultipleSkuId(skuObject.getString("out_sku_sn"));
			skusList.add(skuEntity);
		}

		if (skusList == null || skusList.isEmpty()) {
			throw new ServiceException("skusList为空");
		}
		goods.setSkus(skusList);

		return goods;
	}

	/**
	 * 获取商品SN
	 * 
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	public String getGoodsSn(String goodsId) throws Exception {
		// if (!login) {
		// throw new ServiceException("服务未登录");
		// }
		logger.info("获取商品SN");
		JSONObject responseJson = this.postResult(URLConstant.PDD_GOODS_SN.replace(ParamConstant.PDD_MALLID, mallId) + goodsId, null);
		if (responseJson == null || !responseJson.getBooleanValue("result")) {
			throw new ServiceException("获取商品SN失败【", (responseJson == null ? "" : responseJson.getString("error_msg")), "】");
		}
		return responseJson.getJSONObject("data").getJSONArray("goodsList").getJSONObject(0).getString("goods_sn");
	}

	public String getMallId() {
		return mallId;
	}

	public void setMallId(String mallId) {
		this.mallId = mallId;
	}

	public String getMainCatId() {
		return mainCatId;
	}

	public void setMainCatId(String mainCatId) {
		this.mainCatId = mainCatId;
	}

	@Override
	public String getSellType() {
		return sellType;
	}

	@SuppressWarnings("deprecation")
	private void heart() {
		// CloseableHttpResponse response = null;
		// try {
		// response = httpClient.get(URLConstant.PDD_HEART);
		// HttpUriRequest realRequest = (HttpUriRequest)
		// httpClient.getContext().getAttribute(ExecutionContext.HTTP_REQUEST);
		// if (realRequest.getURI().toString().equals("/Pdd.html")) {
		// if (login) {
		// for (LoginListener listener : loginListeners) {
		// listener.onLogout(this);
		// }
		// }
		// login = false;
		// } else {
		// if (!login) {
		// for (LoginListener listener : loginListeners) {
		// listener.onLogin(this);
		// }
		// }
		// }
		// } catch (IOException e) {
		// logger.error(e, "心跳请求失败");
		// } finally {
		// if (response != null) {
		// try {
		// response.close();
		// } catch (IOException e) {
		// logger.error(e, "心跳请求失败");
		// }
		// }
		// }
	}

	@Override
	public void ship(OrderEntity order) throws Exception {
		checkLogin();
		logger.info("开始发货");

		String expressId = order.getBuyExpressId();
		if (StringUtils.isEmpty(expressId)) {
			throw new ServiceException("快递单号为空");
		}
		logger.debug("expressId【", expressId, "】");

		String expressName = order.getBuyExpressName();
		if (StringUtils.isEmpty(expressName)) {
			throw new ServiceException("快递名称为空");
		}
		logger.debug("expressName【", expressName, "】");

		String shippingId = null;
		String shippingName = null;

		String shippingIdEasy = null;
		String shippingNameEasy = null;

		for (ExpressEntity express : expressList) {
			String _expressName = express.getName();

			if (_expressName.equals(expressName)) {
				shippingId = express.getId();
				shippingName = express.getName();
				break;
			} else {
				if (shippingIdEasy == null) {
					String _expressNameTmp = _expressName.replace("快递", "").replace("物流", "").replace("速递", "");
					if (expressName.startsWith(_expressNameTmp)) {
						shippingIdEasy = express.getId();
						shippingNameEasy = express.getName();
					}
				}
			}
		}
		shippingId = shippingId == null ? shippingIdEasy : shippingId;
		shippingName = shippingName == null ? shippingNameEasy : shippingName;

		logger.debug("shippingId【", shippingId, "】");
		logger.debug("shippingName【", shippingName, "】");
		if (StringUtils.isEmpty(shippingId)) {
			throw new ServiceException("shippingId为空");
		}
		if (StringUtils.isEmpty(shippingName)) {
			throw new ServiceException("shippingName为空");
		}

		JSONObject requestJson = new JSONObject();
		requestJson.put("functionType", 3);
		requestJson.put("isSingleShipment", 1);
		requestJson.put("operateFrom", "MMS");
		requestJson.put("overWrite", 1);
		JSONArray orderShipRequestList = new JSONArray();
		JSONObject orderShipRequest = new JSONObject();
		orderShipRequest.put("deliveryType", 0);
		orderShipRequest.put("importTime", 0);
		orderShipRequest.put("orderSn", order.getSellOrderId());
		orderShipRequest.put("shippingId", shippingId);
		orderShipRequest.put("shippingName", shippingName);
		orderShipRequest.put("trackingNumber", expressId);
		orderShipRequestList.add(orderShipRequest);
		requestJson.put("orderShipRequestList", orderShipRequestList);

		JSONObject responseJson = this.postResult(URLConstant.PDD_ORDER_SHIP, requestJson);
		if (responseJson == null || !responseJson.getBooleanValue("success")) {
			throw new ServiceException("发货失败【", (responseJson == null ? "" : responseJson.getString("error_msg")), "】");
		}
	}

	public void sendChatMessage(String message) throws IOException {
		webSocketClient.sendMessage(message);
	}

	public void chat(ChatListener listener) throws Exception {
		// if (!login) {
		// throw new ServiceException("服务未登录");
		// }
		this.chatListener = listener;
		if (webSocketClient == null) {
			CloseableHttpResponse response = httpClient.post(URLConstant.PDD_CHAT.replace(ParamConstant.DEFAULT_PARAM, mallId) + "1515334230301", "random=10d10ac3f2ab3cd13ac4ab806bc5d311", null);
			JSONObject responseJson = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
			logger.debug("responseJson【", responseJson.toJSONString(), "】");
			String url = responseJson.getString("use_ip") + "/?access_token=" + responseJson.getString("token") + "&role=mall_cs&client=web";
			logger.debug("url【", url, "】");
			webSocketClient = new WebSocketClient();
			webSocketClient.connect(url, 10, TimeUnit.SECONDS);
			webSocketClient.addListener(new WebsSocketListener() {
				@Override
				public void onMessage(String msg) {
					try {
						JSONObject msgJson = JSONObject.parseObject(msg);
						JSONObject messageJson = msgJson.getJSONObject("message");
						if (messageJson == null) {
							return;
						}
						String content = messageJson.getString("content");
						String role = messageJson.getJSONObject("from").getString("role");
						if (role.equals("mall_cs")) {
							String uid = messageJson.getJSONObject("to").getString("uid");
							String nickname = postResult(URLConstant.PDD_CHAT_USER + uid, null).getJSONObject("userinfo").getString("nickname");
							GoodsEntity goods = null;
							OrderEntity order = null;
							JSONObject info = messageJson.getJSONObject("info");
							if (info != null) {
								goods = new GoodsEntity();
								goods.setSellId(info.getString("goodsID"));
								goods.setName(info.getString("goodsName"));
								goods.setCurrentprice(info.getString("goodsPrice"));
								List<String> imagesList = new ArrayList<String>();
								imagesList.add(info.getString("goodsThumbUrl"));
								goods.setCycleImages(imagesList);

								if (info.getString("orderSequenceNo") != null) {
									order = new OrderEntity();
									order.setSellOrderId(info.getString("orderSequenceNo"));
									order.setSellOrderState(info.getInteger("orderStatus"));
									order.setSellAmount(info.getFloat("totalAmount") / 100.00 + "");
								}
							}
							chatListener.onMessage(1, getId(), null, uid, nickname, content, goods, order);
							return;
						}
						if (role.equals("user")) {
							String uid = messageJson.getJSONObject("from").getString("uid");
							String nickname = postResult(URLConstant.PDD_CHAT_USER + uid, null).getJSONObject("userinfo").getString("nickname");
							GoodsEntity goods = null;
							OrderEntity order = null;
							JSONObject info = messageJson.getJSONObject("info");
							if (info != null) {
								goods = new GoodsEntity();
								goods.setSellId(info.getString("goodsID"));
								goods.setName(info.getString("goodsName"));
								goods.setCurrentprice(info.getString("goodsPrice"));
								List<String> imagesList = new ArrayList<String>();
								imagesList.add(info.getString("goodsThumbUrl"));
								goods.setCycleImages(imagesList);

								if (info.getString("orderSequenceNo") != null) {
									order = new OrderEntity();
									order.setSellOrderId(info.getString("orderSequenceNo"));
									order.setSellOrderState(info.getInteger("orderStatus"));
									order.setSellAmount(info.getFloat("totalAmount") / 100.00 + "");
								}
							}
							chatListener.onMessage(0, uid, nickname, getId(), null, content, goods, order);
							return;
						}

						logger.debug("未识别信息【", msg, "】");

					} catch (Exception e) {
						logger.error(e, "解析数据失败【", msg, "】");
					}

				}

				@Override
				public void onConnect(Session session) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onClose(int statusCode, String reason) {
					// TODO Auto-generated method stub

				}
			});
		}

	}

	@Override
	public void offSaleGoods(GoodsEntity goods) throws Exception {
		checkLogin();
		logger.info("开始下架商品");
		JSONObject requestJSON = new JSONObject();
		requestJSON.put("goods_id", goods.getSellId());
		JSONObject responseJSON;
		try {
			responseJSON = this.postResult(URLConstant.PDD_GOODS_OFFSALE, requestJSON);
		} catch (IOException e) {
			throw e;
		}

		if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
			throw new ServiceException("下架商品失败");
		}
	}

}
