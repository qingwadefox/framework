package com.framework.test.pdd.entity;

import java.io.Serializable;
import java.util.List;

public class MultipleSkuEntity implements Serializable {

	private static final long serialVersionUID = 4355583716646937683L;

	private String sellMultipleSkuId;

	private String buyMultipleSkuId;

	// 原价
	private String originalPrice;

	// 团购价
	private String realPrice;

	// 库存
	private String stock;

	// 单个sku 列表
	private List<SimpleSkuEntity> skuList;

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}

	public List<SimpleSkuEntity> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<SimpleSkuEntity> skuList) {
		this.skuList = skuList;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getBuyMultipleSkuId() {
		return buyMultipleSkuId;
	}

	public void setBuyMultipleSkuId(String buyMultipleSkuId) {
		this.buyMultipleSkuId = buyMultipleSkuId;
	}

	public String getSellMultipleSkuId() {
		return sellMultipleSkuId;
	}

	public void setSellMultipleSkuId(String sellMultipleSkuId) {
		this.sellMultipleSkuId = sellMultipleSkuId;
	}

}
