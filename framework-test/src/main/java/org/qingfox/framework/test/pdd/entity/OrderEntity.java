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

import org.apache.commons.lang3.ArrayUtils;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年12月15日
 * @功能说明：
 * 
 */
public class OrderEntity implements Serializable {

	private static final long serialVersionUID = -338216807901655103L;

	private static final String[] BUYORDERSTATE_SHIP_TEXTS = {"物流运输中", "卖家已发货", "快件已揽收"};
	public static final Integer BUYORDERSTATE_UNSHIP = 0;
	public static final Integer BUYORDERSTATE_SHIP = 1;

	public static final Integer SELLORDERSTATE_UNDONE = 1;
	public static final Integer SELLORDERSTATE_UNGROUP = 2;

	private String sellId;
	private String sellOrderId;
	private String sellGoodsId;
	private String sellGoodsSn;
	private String sellGoodsNumber;
	private String sellSkuId;
	private String sellProvinceId;
	private String sellProvinceName;
	private String sellCityId;
	private String sellCityName;
	private String sellDistrictId;
	private String sellDistrictName;
	private String sellReceiveMobile;
	private String sellReceiveName;
	private String sellAddress;
	private String sellMobile;
	private String sellAmount;
	private String sellOrderTime;
	private String sellType;
	private String sellExpressId;
	private String sellExpressName;
	private String sellGoodsSpec;
	private Integer sellOrderState;
	private String buyOrderId;
	private String buyProvinceId;
	private String buyCityId;
	private String buyDistrictId;
	private String buySkuId;
	private String buyUrl;
	private Integer buyOrderState;
	private String buyAmount;
	private String buyOrderTime;
	private String buyExpressId;
	private String buyExpressName;

	public String getBuyOrderId() {
		return buyOrderId;
	}

	/**
	 * @param buyOrderId
	 *            the buyOrderId to set
	 */
	public void setBuyOrderId(String buyOrderId) {
		this.buyOrderId = buyOrderId;
	}

	public String getSellOrderId() {
		return sellOrderId;
	}

	public void setSellOrderId(String sellOrderId) {
		this.sellOrderId = sellOrderId;
	}

	public String getSellGoodsId() {
		return sellGoodsId;
	}

	public void setSellGoodsId(String sellGoodsId) {
		this.sellGoodsId = sellGoodsId;
	}

	public String getSellGoodsSn() {
		return sellGoodsSn;
	}

	public void setSellGoodsSn(String sellGoodsSn) {
		this.sellGoodsSn = sellGoodsSn;
	}

	public String getSellSkuId() {
		return sellSkuId;
	}

	public void setSellSkuId(String sellSkuId) {
		this.sellSkuId = sellSkuId;
	}

	public String getSellProvinceId() {
		return sellProvinceId;
	}

	public void setSellProvinceId(String sellProvinceId) {
		this.sellProvinceId = sellProvinceId;
	}

	public String getSellProvinceName() {
		return sellProvinceName;
	}

	public void setSellProvinceName(String sellProvinceName) {
		this.sellProvinceName = sellProvinceName;
	}

	public String getSellCityId() {
		return sellCityId;
	}

	public void setSellCityId(String sellCityId) {
		this.sellCityId = sellCityId;
	}

	public String getSellCityName() {
		return sellCityName;
	}

	public void setSellCityName(String sellCityName) {
		this.sellCityName = sellCityName;
	}

	public String getSellReceiveMobile() {
		return sellReceiveMobile;
	}

	public void setSellReceiveMobile(String sellReceiveMobile) {
		this.sellReceiveMobile = sellReceiveMobile;
	}

	public String getSellReceiveName() {
		return sellReceiveName;
	}

	public void setSellReceiveName(String sellReceiveName) {
		this.sellReceiveName = sellReceiveName;
	}

	public String getSellAddress() {
		return sellAddress;
	}

	public void setSellAddress(String sellAddress) {
		this.sellAddress = sellAddress;
	}

	public String getSellMobile() {
		return sellMobile;
	}

	public void setSellMobile(String sellMobile) {
		this.sellMobile = sellMobile;
	}

	public String getBuySkuId() {
		return buySkuId;
	}

	public void setBuySkuId(String buySkuId) {
		this.buySkuId = buySkuId;
	}

	public String getBuyUrl() {
		return buyUrl;
	}

	public void setBuyUrl(String buyUrl) {
		this.buyUrl = buyUrl;
	}

	public Integer getBuyOrderState() {
		return buyOrderState;
	}

	public void setBuyOrderState(Integer buyOrderState) {
		this.buyOrderState = buyOrderState;
	}

	public String getBuyOrderTime() {
		return buyOrderTime;
	}

	public void setBuyOrderTime(String buyOrderTime) {
		this.buyOrderTime = buyOrderTime;
	}

	public String getSellDistrictId() {
		return sellDistrictId;
	}

	public void setSellDistrictId(String sellDistrictId) {
		this.sellDistrictId = sellDistrictId;
	}

	public String getSellDistrictName() {
		return sellDistrictName;
	}

	public void setSellDistrictName(String sellDistrictName) {
		this.sellDistrictName = sellDistrictName;
	}

	public String getSellAmount() {
		return sellAmount;
	}

	public void setSellAmount(String sellAmount) {
		this.sellAmount = sellAmount;
	}

	public String getBuyProvinceId() {
		return buyProvinceId;
	}

	public void setBuyProvinceId(String buyProvinceId) {
		this.buyProvinceId = buyProvinceId;
	}

	public String getBuyCityId() {
		return buyCityId;
	}

	public void setBuyCityId(String buyCityId) {
		this.buyCityId = buyCityId;
	}

	public String getBuyDistrictId() {
		return buyDistrictId;
	}

	public void setBuyDistrictId(String buyDistrictId) {
		this.buyDistrictId = buyDistrictId;
	}

	public String getSellGoodsNumber() {
		return sellGoodsNumber;
	}

	public void setSellGoodsNumber(String sellGoodsNumber) {
		this.sellGoodsNumber = sellGoodsNumber;
	}

	public String getSellOrderTime() {
		return sellOrderTime;
	}

	public void setSellOrderTime(String sellOrderTime) {
		this.sellOrderTime = sellOrderTime;
	}

	public String getSellType() {
		return sellType;
	}

	public void setSellType(String sellType) {
		this.sellType = sellType;
	}

	public String getBuyAmount() {
		return buyAmount;
	}

	public void setBuyAmount(String buyAmount) {
		this.buyAmount = buyAmount;
	}

	public String getBuyExpressId() {
		return buyExpressId;
	}

	public void setBuyExpressId(String buyExpressId) {
		this.buyExpressId = buyExpressId;
	}

	public String getBuyExpressName() {
		return buyExpressName;
	}

	public void setBuyExpressName(String buyExpressName) {
		this.buyExpressName = buyExpressName;
	}

	public String getSellExpressId() {
		return sellExpressId;
	}

	public void setSellExpressId(String sellExpressId) {
		this.sellExpressId = sellExpressId;
	}

	public String getSellExpressName() {
		return sellExpressName;
	}

	public void setSellExpressName(String sellExpressName) {
		this.sellExpressName = sellExpressName;
	}

	public Integer getSellOrderState() {
		return sellOrderState;
	}

	public void setSellOrderState(Integer sellOrderState) {
		this.sellOrderState = sellOrderState;
	}

	public String getSellGoodsSpec() {
		return sellGoodsSpec;
	}

	public void setSellGoodsSpec(String sellGoodsSpec) {
		this.sellGoodsSpec = sellGoodsSpec;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public static Integer textTobuyOrderState(String text) {
		if (ArrayUtils.contains(BUYORDERSTATE_SHIP_TEXTS, text)) {
			return BUYORDERSTATE_SHIP;
		} else {
			return BUYORDERSTATE_UNSHIP;
		}
	}

	public static String sellOrderStateToText(Integer sellOrderState) {
		if (sellOrderState.equals(SELLORDERSTATE_UNDONE)) {
			return "未发货";
		} else if (sellOrderState.equals(SELLORDERSTATE_UNGROUP)) {
			return "未拼团";
		}
		return "";
	}

	public static String buyOrderStateToText(Integer buyOrderState) {
		if (buyOrderState != null) {
			if (buyOrderState.equals(BUYORDERSTATE_SHIP)) {
				return "已发货";
			} else if (buyOrderState.equals(BUYORDERSTATE_UNSHIP)) {
				return "未发货";
			}
		}
		return "";
	}
}
