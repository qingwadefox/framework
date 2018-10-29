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
package org.qingfox.framework.microservice.context;

import java.io.Serializable;

import org.qingfox.framework.microservice.enums.RespCode;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see: @创建日期：2017年3月20日 @功能说明：
 * 
 */
public class ServiceResponse<T extends Serializable> implements Serializable {

  private static final long serialVersionUID = -3655843737033626726L;
  private RespCode code;
  private String errorMsg;
  private String errorStack;
  private T result;

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }

  public RespCode getCode() {
    return code;
  }

  public void setCode(RespCode code) {
    this.code = code;
  }

  public String getErrorStack() {
    return errorStack;
  }

  public void setErrorStack(String errorStack) {
    this.errorStack = errorStack;
  }

}
