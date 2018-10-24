/**
 * 
 */
package com.framework.test.pdd.tools;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Point;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.framework.common.beans.Result;
import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.common.utils.FileUtil;
import com.framework.common.utils.ThreadUtil;
import com.framework.test.pdd.constant.URLConstant;
import com.framework.test.pdd.entity.CatEntity;
import com.framework.test.pdd.service.PddService;
import com.framework.test.pdd.utils.WebDriverUtils;

/**
 * @author zhengwei
 *
 */
public class DataCollectionTools {
	private static final ILogger logger = LoggerFactory.getLogger(DataCollectionTools.class);

	// public Result<List<CatEntity>> getCatList() {
	// logger.info("开始爬取cat信息");
	// List<CatEntity> list = new ArrayList<CatEntity>();
	// Result<List<CatEntity>> result = new Result<List<CatEntity>>();
	// JSONObject responseJSON = null;
	//
	// logger.info("爬取第一级cat信息");
	// try {
	// responseJSON = this.postResult(URLConstant.PDD_CAT_LIST + 0, null);
	// } catch (IOException e) {
	// result.setSuccess(false);
	// result.setMessage(e, "爬取第一级cat信息失败");
	// logger.error(result.getMessage());
	// return result;
	// }
	// if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
	// result.setSuccess(false);
	// result.setMessage("爬取第一级cat信息失败【", (responseJSON == null ? "" :
	// responseJSON.getString("error_msg")), "】");
	// logger.error(result.getMessage());
	// return result;
	// }
	//
	// JSONArray lv1Array = responseJSON.getJSONArray("result");
	// for (int i = 0; i < lv1Array.size(); i++) {
	// String lv1Id = lv1Array.getJSONObject(i).getString("id");
	// String lv1Name = lv1Array.getJSONObject(i).getString("cat_name");
	// CatEntity lv1Cat = new CatEntity();
	// lv1Cat.setId(lv1Id);
	// lv1Cat.setName(lv1Name);
	// lv1Cat.setPid("0");
	// lv1Cat.setLv("1");
	// list.add(lv1Cat);
	//
	// logger.info("爬取第二级cat信息");
	// try {
	// responseJSON = this.postResult(URLConstant.PDD_CAT_LIST + lv1Id, null);
	// } catch (IOException e) {
	// result.setSuccess(false);
	// result.setMessage(e, "爬取第二级cat信息失败");
	// logger.error(result.getMessage());
	// return result;
	// }
	// if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
	// result.setSuccess(false);
	// result.setMessage("爬取第二级cat信息失败【", (responseJSON == null ? "" :
	// responseJSON.getString("error_msg")), "】");
	// logger.error(result.getMessage());
	// return result;
	// }
	// JSONArray lv2Array = responseJSON.getJSONArray("result");
	// for (int j = 0; j < lv2Array.size(); j++) {
	// String lv2Id = lv2Array.getJSONObject(j).getString("id");
	// String lv2Name = lv2Array.getJSONObject(j).getString("cat_name");
	// CatEntity lv2Cat = new CatEntity();
	// lv2Cat.setId(lv2Id);
	// lv2Cat.setName(lv2Name);
	// lv2Cat.setPid(lv1Id);
	// lv2Cat.setLv("2");
	// lv2Cat.setRootId(lv1Id);
	// list.add(lv2Cat);
	//
	// logger.info("爬取第三级cat信息");
	// try {
	// responseJSON = this.postResult(URLConstant.PDD_CAT_LIST + lv2Id, null);
	// } catch (IOException e) {
	// result.setSuccess(false);
	// result.setMessage(e, "爬取第三级cat信息失败");
	// logger.error(result.getMessage());
	// return result;
	// }
	// if (responseJSON == null || !responseJSON.getBooleanValue("success")) {
	// result.setSuccess(false);
	// result.setMessage("爬取第三级cat信息失败【", (responseJSON == null ? "" :
	// responseJSON.getString("error_msg")), "】");
	// logger.error(result.getMessage());
	// return result;
	// }
	//
	// JSONArray lv3Array = responseJSON.getJSONArray("result");
	// for (int k = 0; k < lv3Array.size(); k++) {
	// String lv3Id = lv3Array.getJSONObject(k).getString("id");
	// String lv3Name = lv3Array.getJSONObject(k).getString("cat_name");
	// CatEntity lv3Cat = new CatEntity();
	// lv3Cat.setId(lv3Id);
	// lv3Cat.setName(lv3Name);
	// lv3Cat.setPid(lv2Id);
	// lv3Cat.setLv("3");
	// lv3Cat.setRootId(lv1Id);
	// list.add(lv3Cat);
	// }
	// }
	// }
	// result.setResult(list);
	// return result;
	// }

	public static void ExpressData(File expressData) {

	}

	public static void RegionMatch(File sellRegionDataFile, File buyRegionDataFile, File resultFile) throws IOException {

		List<String> sellRegionLines = FileUtils.readLines(sellRegionDataFile);
		List<String> buyRegionLines = FileUtils.readLines(buyRegionDataFile);
		List<String> matchLvList = new ArrayList<String>();
		matchLvList.add("1");
		matchLvList.add("2");
		matchLvList.add("3");

		Map<String, JSONObject> matchRegionMap = new HashMap<String, JSONObject>();
		int notMatchCount = 0;
		for (String sellRegionLine : sellRegionLines) {
			JSONObject sellRegionJson = JSONObject.parseObject(sellRegionLine);
			String sellRegionLv = sellRegionJson.getString("region_type");
			if (!matchLvList.contains(sellRegionLv)) {
				continue;
			}
			String sellRegionId = sellRegionJson.getString("id");
			String sellRegionName = sellRegionJson.getString("region_name");

			int matchCount = 0;
			String buyRegionId = null;
			String buyRegionName = null;
			String buyRegionParentId = null;
			String buyRegionLv = null;
			for (String buyRegionLine : buyRegionLines) {
				JSONObject buyRegionJson = JSONObject.parseObject(buyRegionLine);
				String _buyRegionName = buyRegionJson.getString("title");
				String _buyRegionLv = buyRegionJson.getString("lv");
				if (!sellRegionLv.equals(_buyRegionLv)) {
					continue;
				}

				if (!sellRegionName.equals(_buyRegionName)) {

					String _sellRegionName = sellRegionName;
					if (_sellRegionName.endsWith("县") || _sellRegionName.endsWith("盟") || _sellRegionName.endsWith("市")) {
						_sellRegionName = _sellRegionName.substring(0, _sellRegionName.length() - 1);
					} else if (_sellRegionName.equals("延边")) {
						_sellRegionName = "延边朝鲜族";
					} else if (_sellRegionName.equals("神农架林区")) {
						_sellRegionName = "神农架";
					} else if (_sellRegionName.equals("阿拉善盟")) {
						_sellRegionName = "阿拉善";
					} else if (_sellRegionName.equals("克孜勒苏")) {
						_sellRegionName = "克孜勒苏柯尔克孜";
					} else if (_sellRegionName.equals("塔城地区")) {
						_sellRegionName = "塔城";
					} else if (_sellRegionName.equals("海南")) {
						_sellRegionName = "海南藏族";
					}

					if (!_sellRegionName.equals(_buyRegionName)) {
						continue;
					}
				}
				buyRegionName = buyRegionJson.getString("title");
				buyRegionLv = buyRegionJson.getString("lv");
				buyRegionId = buyRegionJson.getString("attr_id");
				buyRegionParentId = buyRegionJson.getString("parent_attr_id");
				matchCount++;
			}

			if (matchCount == 0) {
				notMatchCount++;
				System.out.println(sellRegionJson.getString("region_name") + "未查找到匹配");
			}

			if (matchCount > 1) {
				notMatchCount++;
				System.out.println(sellRegionJson.getString("region_name") + "查找到匹配 【" + matchCount + "】个");
			}
			if (matchCount == 1) {
				sellRegionJson.put("buyId", buyRegionId);
				sellRegionJson.put("buyName", buyRegionName);
				sellRegionJson.put("buyParentId", buyRegionParentId);
				sellRegionJson.put("buyLv", buyRegionLv);
				matchRegionMap.put(sellRegionId, sellRegionJson);
			}
		}
		System.out.println("未匹配数量为【" + notMatchCount + "】");

		int matchFailCount = 0;
		for (JSONObject matchRegionJson : matchRegionMap.values()) {
			String sellRegionName = matchRegionJson.getString("region_name");
			String sellRegionParentId = matchRegionJson.getString("parent_id");
			String sellLv = matchRegionJson.getString("region_type");
			String buyParentId = matchRegionJson.getString("buyParentId");

			if (sellLv.equals("2")) {
				JSONObject parentMatchRegionJson = matchRegionMap.get(sellRegionParentId);
				if (!parentMatchRegionJson.getString("buyId").equals(buyParentId)) {
					System.out.println(sellRegionName + "匹配父ID失败");
					matchFailCount++;
				}
			}
		}
		System.out.println("匹配失败数量为【" + matchFailCount + "】");

		FileUtil.clearText(resultFile);
		for (String sellRegionLine : sellRegionLines) {
			JSONObject sellRegionJson = JSONObject.parseObject(sellRegionLine);
			String sellRegionId = sellRegionJson.getString("id");
			JSONObject matchRegionJson = matchRegionMap.get(sellRegionId);
			JSONObject writeJson = new JSONObject();
			if (matchRegionJson != null) {
				writeJson.put("buyRegionId", matchRegionJson.getString("buyId"));
				writeJson.put("buyRegionName", matchRegionJson.getString("buyName"));
				writeJson.put("buyRegionParentId", matchRegionJson.getString("buyParentId"));
				writeJson.put("buyRegionLv", matchRegionJson.getString("buyLv"));
			}

			writeJson.put("sellRegionId", sellRegionJson.getString("id"));
			writeJson.put("sellRegionName", sellRegionJson.getString("region_name"));
			writeJson.put("sellRegionParentId", sellRegionJson.getString("parent_id"));
			writeJson.put("sellRegionLv", sellRegionJson.getString("region_type"));
			FileUtil.appendLine(resultFile, writeJson.toJSONString());
		}

	}

	public static void main(String[] args) throws IOException {
		File sellRegionDataFile = new File("D:\\pddproject\\data\\newPddRegion2.dat");
		File buyRegionDataFile = new File("D:\\pddproject\\data\\taobaoRegion.dat");
		File resultFile = new File("D:\\pddproject\\data\\result.dat");
		RegionMatch(sellRegionDataFile, buyRegionDataFile, resultFile);
	}
	// public Result<Void> simpleLogin() {
	// driver.manage().deleteAllCookies();
	// logger.info("开始模拟登录操作");
	// Result<Void> result = new Result<Void>();
	// driver.get(TaobaoConstant.PAGE_LOGIN_URL);
	// ThreadUtil.sleep(1000);
	// logger.info("切换用户密码登录");
	// Point point = WebDriverUtils.id(driver,
	// TaobaoConstant.PAGE_LOGIN_EL_SWITCH_ID).getLocation();
	// try {
	// Robot myRobot = new Robot();
	// myRobot.mouseMove(point.x, point.y);
	// } catch (AWTException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return result;
	// }
	//
	// public Result<List<CatEntity>> getCatListTmp(List<CatEntity> lv3List) {
	// Result<List<CatEntity>> result = new Result<List<CatEntity>>();
	// List<CatEntity> lv4List = new ArrayList<CatEntity>();
	// int i = 0;
	// for (CatEntity lv3Cat : lv3List) {
	// i++;
	// System.out.println(i + " / " + lv3List.size());
	// String lv3Id = lv3Cat.getId();
	// String lv1Id = lv3Cat.getRootId();
	// if (lv3Id.indexOf(":") != -1) {
	// continue;
	// }
	//
	// // lv4
	// driver.get(TaobaoConstant.PAGE_DATA_CAT_LIST_URL + lv3Id);
	// String pageBody = WebDriverUtils.waitTagname(driver, "body").getText();
	// JSONArray lv4Array = null;
	// try {
	// lv4Array =
	// JSONArray.parseArray(pageBody).getJSONObject(0).getJSONArray("data");
	// } catch (Exception e) {
	// result.setMessage("解析数据失败pid【", lv3Id, "】【", pageBody, "】");
	// continue;
	// }
	//
	// if (lv4Array.isEmpty()) {
	// continue;
	// }
	//
	// for (int m = 0; m < lv4Array.size(); m++) {
	// JSONArray lv4Data = lv4Array.getJSONObject(m).getJSONArray("data");
	// if (lv4Data.isEmpty()) {
	// continue;
	// }
	//
	// for (int n = 0; n < lv4Data.size(); n++) {
	// String lv4Id = lv4Data.getJSONObject(n).getString("sid");
	// String lv4Name = lv4Data.getJSONObject(n).getString("name");
	// CatEntity lv4Cat = new CatEntity();
	// lv4Cat.setId(lv4Id);
	// lv4Cat.setName(lv4Name);
	// lv4Cat.setPid(lv3Id);
	// lv4Cat.setLv("4");
	// lv4Cat.setRootId(lv1Id);
	// lv4List.add(lv4Cat);
	// }
	// }
	// }
	// result.setResult(lv4List);
	// return result;
	// }
	//
	// /**
	// * 爬取cat信息 .
	// *
	// * @return
	// * @author Administrator 2017年12月20日 Administrator
	// */
	// public Result<List<CatEntity>> getCatList() {
	// logger.info("开始爬取cat信息");
	// List<CatEntity> list = new ArrayList<CatEntity>();
	// Result<List<CatEntity>> result = new Result<List<CatEntity>>();
	// String pageBody = null;
	//
	// // lv1
	// driver.get(TaobaoConstant.PAGE_DATA_CAT_LIST_URL);
	// pageBody = WebDriverUtils.waitTagname(driver, "body").getText();
	// JSONArray lv1Array = null;
	//
	// try {
	// lv1Array =
	// JSONArray.parseArray(pageBody).getJSONObject(0).getJSONArray("data");
	// } catch (Exception e) {
	// result.setMessage("解析数据失败【", pageBody, "】");
	// result.setSuccess(false);
	// logger.error(result.getMessage());
	// return result;
	// }
	// for (int i = 0; i < lv1Array.size(); i++) {
	// logger.debug("爬取进度：", i, "/", lv1Array.size());
	// String lv1Id = lv1Array.getJSONObject(i).getString("id");
	// String lv1Name = lv1Array.getJSONObject(i).getString("name");
	// JSONArray lv1Data = lv1Array.getJSONObject(i).getJSONArray("data");
	// CatEntity lv1Cat = new CatEntity();
	// lv1Cat.setId(lv1Id);
	// lv1Cat.setName(lv1Name);
	// lv1Cat.setPid("0");
	// lv1Cat.setLv("1");
	// list.add(lv1Cat);
	//
	// if (lv1Data.isEmpty()) {
	// logger.warn("id【", lv1Id, "】name【", lv1Name, "】不存在子内容");
	// continue;
	// }
	//
	// // lv2
	// for (int j = 0; j < lv1Data.size(); j++) {
	// String lv2Id = lv1Data.getJSONObject(j).getString("sid");
	// String lv2Name = lv1Data.getJSONObject(j).getString("name");
	// CatEntity lv2Cat = new CatEntity();
	// lv2Cat.setId(lv2Id);
	// lv2Cat.setName(lv2Name);
	// lv2Cat.setPid(lv1Id);
	// lv2Cat.setLv("2");
	// lv2Cat.setRootId(lv1Id);
	// list.add(lv2Cat);
	//
	// // lv3
	// driver.get(TaobaoConstant.PAGE_DATA_CAT_LIST_URL + lv2Id);
	// pageBody = WebDriverUtils.waitTagname(driver, "body").getText();
	// JSONArray lv3Array = null;
	// try {
	// lv3Array =
	// JSONArray.parseArray(pageBody).getJSONObject(0).getJSONArray("data");
	// } catch (Exception e) {
	// result.setMessage("解析数据失败pid【", lv2Id, "】【", pageBody, "】");
	// continue;
	// }
	//
	// if (lv3Array.isEmpty()) {
	// continue;
	// }
	//
	// for (int k = 0; k < lv3Array.size(); k++) {
	// JSONArray lv3Data = lv3Array.getJSONObject(k).getJSONArray("data");
	// if (lv3Data.isEmpty()) {
	// continue;
	// }
	//
	// for (int l = 0; l < lv3Data.size(); l++) {
	// String lv3Id = lv3Data.getJSONObject(l).getString("sid");
	// String lv3Name = lv3Data.getJSONObject(l).getString("name");
	// CatEntity lv3Cat = new CatEntity();
	// lv3Cat.setId(lv3Id);
	// lv3Cat.setName(lv3Name);
	// lv3Cat.setPid(lv2Id);
	// lv3Cat.setLv("3");
	// lv3Cat.setRootId(lv1Id);
	// list.add(lv3Cat);
	//
	// // lv4
	// driver.get(TaobaoConstant.PAGE_DATA_CAT_LIST_URL + lv3Id);
	// pageBody = WebDriverUtils.waitTagname(driver, "body").getText();
	// JSONArray lv4Array = null;
	// try {
	// lv4Array =
	// JSONArray.parseArray(pageBody).getJSONObject(0).getJSONArray("data");
	// } catch (Exception e) {
	// result.setMessage("解析数据失败pid【", lv3Id, "】【", pageBody, "】");
	// continue;
	// }
	//
	// if (lv4Array.isEmpty()) {
	// continue;
	// }
	//
	// for (int m = 0; m < lv4Array.size(); m++) {
	// JSONArray lv4Data = lv4Array.getJSONObject(m).getJSONArray("data");
	// if (lv4Data.isEmpty()) {
	// continue;
	// }
	//
	// for (int n = 0; n < lv4Data.size(); n++) {
	// String lv4Id = lv4Data.getJSONObject(n).getString("sid");
	// String lv4Name = lv4Data.getJSONObject(n).getString("name");
	// CatEntity lv4Cat = new CatEntity();
	// lv4Cat.setId(lv4Id);
	// lv4Cat.setName(lv4Name);
	// lv4Cat.setPid(lv3Id);
	// lv4Cat.setLv("4");
	// lv4Cat.setRootId(lv1Id);
	// list.add(lv4Cat);
	// }
	// }
	//
	// }
	// }
	//
	// }
	// }
	// result.setResult(list);
	// return result;
	//
	// }
}
