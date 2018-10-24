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
package com.framework.test.pdd.threads;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.framework.common.exceptions.ServiceException;
import com.framework.common.exceptions.ThreadExitException;
import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.common.thread.TaskThread;
import com.framework.common.utils.DateUtil;
import com.framework.common.utils.FileUtil;
import com.framework.common.utils.ReadLine;
import com.framework.common.utils.ThreadUtil;
import com.framework.test.pdd.constant.PathConstant;
import com.framework.test.pdd.entity.GoodsEntity;
import com.framework.test.pdd.service.inf.ICommissionService;
import com.framework.test.pdd.service.inf.IDatamineService;
import com.framework.test.pdd.service.inf.ISellService;
import com.framework.test.pdd.threads.listener.ISellServiceTaskListener;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see: @创建日期：2018年1月19日 @功能说明：
 * 
 */
public class SellServiceTask extends TaskThread {
	private static final ILogger logger = LoggerFactory.getLogger(SellServiceTask.class);

	// 检测状态 0-未检测；1-检测中；2-检测成功；3-检测失败；
	private Integer checkState;
	private Integer checkSuccess;
	private Integer checkFailure;

	// 发布状态 0-未发布；1-发布中；2-发布成功；3-发布失败；
	private Integer publicState;
	private Integer publicSuccess;
	private Integer publicFailure;

	private Long lastTaskTime;

	private ISellService sellService;
	private ICommissionService commissionService;
	private IDatamineService datamineService;

	private List<GoodsEntity> onsellGoodsList;

	private List<ISellServiceTaskListener> listeners;

	public SellServiceTask(ISellService sellService) {
		this.listeners = new ArrayList<>();
		this.sellService = sellService;
		this.getTaskInfoCache();
		this.pause();
		this.start();
	}

	@Override
	protected void runTask() throws ThreadExitException, IOException {
		while (true) {
			check();
			if (this.checkState == 2 && this.publicState == 2) {
				if (DateUtil.current(DateUtil.PATTERN_YYYYMMDD).equals(DateUtil.format(lastTaskTime, DateUtil.PATTERN_YYYYMMDD))) {
					this.checkState = 0;
					this.publicState = 0;
					this.writeTaskCache();
				} else {
					ThreadUtil.sleep(120000);
					continue;
				}
			}
			if (this.checkState != 2) {
				runGoodsCheckTask();
			}

			if (this.checkState != 2) {
				continue;
			}

			if (onsellGoodsList == null || onsellGoodsList.isEmpty()) {
				onsellGoodsList = new ArrayList<>();
				File onsellGoodsCheckFile = new File(PathConstant.DIR_ROOT + File.separator + PathConstant.DIR_CACHE + File.separator + DateUtil.currentDate() + File.separator + sellService.getUsername() + "_ONSELLGOODS_LIST_CHECK.dat");
				if(!onsellGoodsCheckFile.exists()) {
					this.checkState = 0;
					this.publicState = 0;
					continue;
				}
				try {
					FileUtil.readLine(onsellGoodsCheckFile, new ReadLine<Void>() {
						@Override
						public void nextLine(String line, int number) {
							onsellGoodsList.add(JSONObject.parseObject(line, GoodsEntity.class));
						}
					});
				} catch (Exception e) {
					logger.error(e, "读取验证后在售商品缓存文件失败");
					continue;
				}
			}

			if (this.publicState != 2) {
				runPublicTask();
			}

		}

	}

	private void runGoodsCheckTask() throws ThreadExitException {

		check();
		this.checkState = 1;
		this.writeTaskCache();

		// 读取在售商品列表
		onsellGoodsList = new ArrayList<GoodsEntity>();
		File onsellGoodsCheckFile = new File(PathConstant.DIR_ROOT + File.separator + PathConstant.DIR_CACHE + File.separator + DateUtil.currentDate() + File.separator + sellService.getUsername() + "_ONSELLGOODS_LIST_CHECK.dat");
		if (onsellGoodsCheckFile.exists()) {
			try {
				FileUtil.readLine(onsellGoodsCheckFile, new ReadLine<Void>() {
					@Override
					public void nextLine(String line, int number) {
						onsellGoodsList.add(JSONObject.parseObject(line, GoodsEntity.class));
					}
				});
			} catch (Exception e) {
				logger.error(e, "读取在售商品缓存文件失败");
				this.checkState = 3;
				this.writeTaskCache();
				return;
			}
		} else {
			this.checkFailure = 0;
			this.checkSuccess = 0;
			this.writeTaskCache();
			File onsellGoodsFile = new File(PathConstant.DIR_ROOT + File.separator + PathConstant.DIR_CACHE + File.separator + DateUtil.currentDate() + File.separator + sellService.getUsername() + "_ONSELLGOODS_LIST.dat");
			if (onsellGoodsFile.exists()) {
				try {
					FileUtil.readLine(onsellGoodsFile, new ReadLine<Void>() {
						@Override
						public void nextLine(String line, int number) {
							onsellGoodsList.add(JSONObject.parseObject(line, GoodsEntity.class));
						}
					});
				} catch (Exception e) {
					logger.error(e, "读取在售商品缓存文件失败");
					this.checkState = 3;
					this.writeTaskCache();
					return;
				}
			} else {
				try {
					onsellGoodsList = sellService.getGoodsList();
					for (GoodsEntity goods : onsellGoodsList) {
						FileUtil.appendLine(onsellGoodsFile, JSONObject.toJSONString(goods));
					}
				} catch (Exception e) {
					logger.error(e, "获取在售商品失败");
					this.checkState = 3;
					this.writeTaskCache();
					return;
				}
			}

			List<GoodsEntity> removeList = new ArrayList<GoodsEntity>();
			for (GoodsEntity goods : onsellGoodsList) {
				this.writeTaskCache();
				Float commission = null;
				try {
					commission = datamineService.getGoodsCommission(goods.getBuyId());
					logger.debug("commission【" + commission + "】");
				} catch (Exception e) {
					logger.error(e, "获取商品【", goods.getBuyId(), "】佣金失败");
					this.checkFailure++;
					continue;
				}
				if (commission == null) {
					try {
						sellService.offSaleGoods(goods);
						this.checkSuccess++;
						this.writeTaskCache();
					} catch (Exception e) {
						logger.error(e, "下架商品失败");
						this.checkFailure++;
						this.writeTaskCache();
					}
					removeList.add(goods);
				}
			}
			onsellGoodsList.removeAll(removeList);

			for (GoodsEntity goods : onsellGoodsList) {
				try {
					FileUtil.appendLine(onsellGoodsCheckFile, JSONObject.toJSONString(goods));
				} catch (IOException e) {
					logger.error(e, "写入文件失败");
				}
			}
			FileUtils.deleteQuietly(onsellGoodsFile);
		}

		this.checkState = 2;
		this.writeTaskCache();
	}

	private void runPublicTask() throws ThreadExitException, IOException {

		// 获取缓存任务信息
		check();
		this.publicState = 1;
		this.writeTaskCache();

		// 开始发布商品
		check();
		int page = 1;
		while (true) {
			check();
			if (this.publicState == 2) {
				logger.info("发布任务已经完成");
				return;
			}
			logger.info("开始读取需要发布商品");
			List<String> publicGoodsList = new ArrayList<String>();
			try {
				publicGoodsList = datamineService.getGoodsList(sellService.getSellType(), page);
			} catch (Exception e) {
				logger.error(e, "获取商品失败");
				ThreadUtil.sleep(2000);
				continue;
			}

			if (publicGoodsList.isEmpty()) {
				logger.info("根据page【", page, "】返回数据为空");
				ThreadUtil.sleep(2000);
				continue;
			}
			logger.debug("获取到商品【", publicGoodsList.size(), "】个");

			for (String goodsStr : publicGoodsList) {
				check();
				if (this.publicState == 2) {
					logger.info("发布任务已经完成");
					return;
				}
				logger.info("开始发布商品【", goodsStr, "】");
				GoodsEntity goods = JSONObject.parseObject(goodsStr, GoodsEntity.class);

				logger.info("检查商品是否已被发布");
				boolean hasGoods = false;
				for (GoodsEntity onsellGoods : onsellGoodsList) {
					if (goods.getBuyId().equals(onsellGoods.getBuyId())) {
						hasGoods = true;
						break;
					}
				}
				if (hasGoods) {
					logger.debug("商品ID【", goods.getBuyId(), "】已被发布");
					continue;
				}

				logger.info("获取商品推广地址");
				String commissionUrl = null;
				try {
					commissionUrl = commissionService.getCommission(goods);
				} catch (Exception e) {
					logger.error(e, "获取推广地址失败");
					this.publicFailure++;
					this.writeTaskCache();
					continue;
				}
				if (StringUtils.isEmpty(commissionUrl)) {
					logger.error("获取推广地址失败");
					this.publicFailure++;
					this.writeTaskCache();
					continue;
				}
				goods.setBuyUrl(commissionUrl);

				logger.info("开始发布商品");
				try {
					sellService.createGoods(goods);
					this.publicSuccess++;
				} catch (ServiceException e) {
					this.publicFailure++;
					logger.error(e, "创建商品失败");
					if (StringUtils.isNotEmpty(e.getCode()) && e.getCode().equals("900013")) {
						logger.info("商品已经达到发布限额");
						this.publicState = 2;
						this.writeTaskCache();
						break;
					}
				} catch (Exception e) {
					this.publicFailure++;
					logger.warn(e, "创建商品失败");
				}finally {
					this.writeTaskCache();
				}

			}
			page++;
		}
	}

	private void getTaskInfoCache() {
		logger.info("读取任务缓存文件");
		File taskInfoFile = new File(PathConstant.DIR_ROOT + File.separator + PathConstant.DIR_CACHE + File.separator + DateUtil.currentDate() + File.separator + sellService.getUsername() + "_TASK_INFO.dat");
		try {
			JSONObject taskInfoJson = JSONObject.parseObject(FileUtils.readFileToString(taskInfoFile));
			this.checkState = taskInfoJson.getInteger("checkState");
			this.checkSuccess = taskInfoJson.getInteger("checkSuccess");
			this.checkFailure = taskInfoJson.getInteger("checkFailure");
			this.publicState = taskInfoJson.getInteger("publicState");
			this.publicSuccess = taskInfoJson.getInteger("publicSuccess");
			this.publicFailure = taskInfoJson.getInteger("publicFailure");
			this.lastTaskTime = taskInfoJson.getLong("lastTaskTime");
		} catch (Exception e) {
			this.checkState = 0;
			this.checkSuccess = 0;
			this.checkFailure = 0;
			this.publicState = 0;
			this.publicSuccess = 0;
			this.publicFailure = 0;
			this.lastTaskTime = System.currentTimeMillis();
		}
	}

	private void writeTaskCache() {
		logger.info("写入任务缓存文件");
		this.lastTaskTime = System.currentTimeMillis();
		File taskInfoFile = new File(PathConstant.DIR_ROOT + File.separator + PathConstant.DIR_CACHE + File.separator + DateUtil.currentDate() + File.separator + sellService.getUsername() + "_TASK_INFO.dat");
		JSONObject taskInfoJson = new JSONObject();
		taskInfoJson.put("checkState", this.checkState);
		taskInfoJson.put("checkSuccess", this.checkSuccess);
		taskInfoJson.put("checkFailure", this.checkFailure);
		taskInfoJson.put("publicState", this.publicState);
		taskInfoJson.put("publicSuccess", this.publicSuccess);
		taskInfoJson.put("publicFailure", this.publicFailure);
		taskInfoJson.put("lastTaskTime", this.lastTaskTime);
		for (ISellServiceTaskListener listener : listeners) {
			listener.onTaskStateChange(this, checkState, checkSuccess, checkFailure, publicState, publicSuccess, publicFailure, lastTaskTime);
		}
		try {
			FileUtils.write(taskInfoFile, taskInfoJson.toJSONString());
		} catch (IOException e) {
			logger.error(e, "写入任务缓存文件【", taskInfoFile.getPath(), "】失败");
		}

	}

	public void addListener(ISellServiceTaskListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(ISellServiceTaskListener listener) {
		this.listeners.remove(listener);
	}

	public void setCommissionService(ICommissionService commissionService) {
		this.commissionService = commissionService;
	}

	public void setDatamineService(IDatamineService datamineService) {
		this.datamineService = datamineService;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public Integer getCheckSuccess() {
		return checkSuccess;
	}

	public Integer getCheckFailure() {
		return checkFailure;
	}

	public Integer getPublicState() {
		return publicState;
	}

	public Integer getPublicSuccess() {
		return publicSuccess;
	}

	public Integer getPublicFailure() {
		return publicFailure;
	}

	public Long getLastTaskTime() {
		return lastTaskTime;
	}

	public ISellService getSellService() {
		return sellService;
	}

	public List<GoodsEntity> getOnsellGoodsList() {
		return onsellGoodsList;
	}

}
