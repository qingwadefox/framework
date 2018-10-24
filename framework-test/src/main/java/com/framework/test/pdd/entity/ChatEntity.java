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
import java.util.List;
import java.util.Map;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2018年1月8日
 * @功能说明：
 * 
 */
public class ChatEntity implements Serializable {

	private static final long serialVersionUID = -2486944864933307749L;

	private String id;
	private String nickname;
	private String userId;
	private List<Map<String, Object>> unReadChatList;
	private List<Map<String, Object>> readChatList;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public List<Map<String, Object>> getUnReadChatList() {
		return unReadChatList;
	}

	public void setUnReadChatList(List<Map<String, Object>> unReadChatList) {
		this.unReadChatList = unReadChatList;
	}

	public List<Map<String, Object>> getReadChatList() {
		return readChatList;
	}

	public void setReadChatList(List<Map<String, Object>> readChatList) {
		this.readChatList = readChatList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
