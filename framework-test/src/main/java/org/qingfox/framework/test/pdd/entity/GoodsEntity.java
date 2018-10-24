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
package org.qingfox.framework.test.pdd.entity;

import java.io.Serializable;
import java.util.List;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see: @创建日期：2017年12月13日 @功能说明：
 * 
 */
public class GoodsEntity implements Serializable {
	private static final long serialVersionUID = 944708916215139349L;

	private String name;
	private String desc;
	private String currentprice;
	private String goodsDesc;
	private List<String> cycleImages;
	private List<String> descImages;
	private List<MultipleSkuEntity> skus;

	private Boolean publicSuccess;
	private String publicErrorMsg;
	public Long publicTime;

	private String sellId;
	private String sellSn;
	private String sellCatId;

	private String buyUrl;
	private String buyId;
	private String buyType;
	private String buyCatId;

	private Float commission;

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public String getSellSn() {
		return sellSn;
	}

	public void setSellSn(String sellSn) {
		this.sellSn = sellSn;
	}

	public String getSellCatId() {
		return sellCatId;
	}

	public void setSellCatId(String sellCatId) {
		this.sellCatId = sellCatId;
	}

	public String getBuyId() {
		return buyId;
	}

	public void setBuyId(String buyId) {
		this.buyId = buyId;
	}

	public String getBuyCatId() {
		return buyCatId;
	}

	public void setBuyCatId(String buyCatId) {
		this.buyCatId = buyCatId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCurrentprice() {
		return currentprice;
	}

	public void setCurrentprice(String currentprice) {
		this.currentprice = currentprice;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public List<String> getCycleImages() {
		return cycleImages;
	}

	public void setCycleImages(List<String> cycleImages) {
		this.cycleImages = cycleImages;
	}

	public List<String> getDescImages() {
		return descImages;
	}

	public void setDescImages(List<String> descImages) {
		this.descImages = descImages;
	}

	public List<MultipleSkuEntity> getSkus() {
		return skus;
	}

	public void setSkus(List<MultipleSkuEntity> skus) {
		this.skus = skus;
	}

	public Boolean getPublicSuccess() {
		return publicSuccess;
	}

	public void setPublicSuccess(Boolean publicSuccess) {
		this.publicSuccess = publicSuccess;
	}

	public String getPublicErrorMsg() {
		return publicErrorMsg;
	}

	public void setPublicErrorMsg(String publicErrorMsg) {
		this.publicErrorMsg = publicErrorMsg;
	}

	public Long getPublicTime() {
		return publicTime;
	}

	public void setPublicTime(Long publicTime) {
		this.publicTime = publicTime;
	}

	public String getBuyUrl() {
		return buyUrl;
	}

	public void setBuyUrl(String buyUrl) {
		this.buyUrl = buyUrl;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public Float getCommission() {
		return commission;
	}

	public void setCommission(Float commission) {
		this.commission = commission;
	}

}
