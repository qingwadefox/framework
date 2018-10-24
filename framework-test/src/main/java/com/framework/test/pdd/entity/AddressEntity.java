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
package com.framework.test.pdd.entity;

import java.io.Serializable;

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
public class AddressEntity implements Serializable {

	private static final long serialVersionUID = -801935190622352457L;

	private String country;
	private String province;
	private String city;
	private String area;
	private String street;
	private String detailed;
	private String postcode;
	private String recname;
	private String mobilecountry;
	private String mobile;
	private String telcountry;
	private String telareacode;
	private String telnumber;
	private String telextension;
	/**
	 * @return country属性
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country
	 *            设置country属性
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return province属性
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province
	 *            设置province属性
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * @return city属性
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city
	 *            设置city属性
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return area属性
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area
	 *            设置area属性
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return street属性
	 */
	public String getStreet() {
		return street;
	}
	/**
	 * @param street
	 *            设置street属性
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	/**
	 * @return detailed属性
	 */
	public String getDetailed() {
		return detailed;
	}
	/**
	 * @param detailed
	 *            设置detailed属性
	 */
	public void setDetailed(String detailed) {
		this.detailed = detailed;
	}
	/**
	 * @return postcode属性
	 */
	public String getPostcode() {
		return postcode;
	}
	/**
	 * @param postcode
	 *            设置postcode属性
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	/**
	 * @return recname属性
	 */
	public String getRecname() {
		return recname;
	}
	/**
	 * @param recname
	 *            设置recname属性
	 */
	public void setRecname(String recname) {
		this.recname = recname;
	}
	/**
	 * @return mobilecountry属性
	 */
	public String getMobilecountry() {
		return mobilecountry;
	}
	/**
	 * @param mobilecountry
	 *            设置mobilecountry属性
	 */
	public void setMobilecountry(String mobilecountry) {
		this.mobilecountry = mobilecountry;
	}
	/**
	 * @return mobile属性
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile
	 *            设置mobile属性
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return telcountry属性
	 */
	public String getTelcountry() {
		return telcountry;
	}
	/**
	 * @param telcountry
	 *            设置telcountry属性
	 */
	public void setTelcountry(String telcountry) {
		this.telcountry = telcountry;
	}
	/**
	 * @return telareacode属性
	 */
	public String getTelareacode() {
		return telareacode;
	}
	/**
	 * @param telareacode
	 *            设置telareacode属性
	 */
	public void setTelareacode(String telareacode) {
		this.telareacode = telareacode;
	}
	/**
	 * @return telnumber属性
	 */
	public String getTelnumber() {
		return telnumber;
	}
	/**
	 * @param telnumber
	 *            设置telnumber属性
	 */
	public void setTelnumber(String telnumber) {
		this.telnumber = telnumber;
	}
	/**
	 * @return telextension属性
	 */
	public String getTelextension() {
		return telextension;
	}
	/**
	 * @param telextension
	 *            设置telextension属性
	 */
	public void setTelextension(String telextension) {
		this.telextension = telextension;
	}

}
