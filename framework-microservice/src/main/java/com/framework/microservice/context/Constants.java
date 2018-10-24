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
package com.framework.microservice.context;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年4月11日
 * @功能说明：
 * 
 */
public class Constants {

	/**
	 * 服务前缀
	 */
	public final static String PREFIX_SERVICE = "service";

	/**
	 * SYSDIR的名称
	 */
	public final static String ENV_SYSDIR = "SYSDIR";

	/**
	 * 微服务容器的端口
	 */
	public final static String ENV_SERVICE_PORT = "DCS_MICROSERVICE_PORT";

	/**
	 * K8S的POD名称
	 */
	public final static String ENV_POD_NAME = "FCSP_POD_NAME";

	/**
	 * 宿主机IP
	 */
	public final static String ENV_FCSP_HOST_IP = "FCSP_HOST_IP";

	/**
	 * PODIP
	 */
	public final static String ENV_FCSP_POD_IP = "FCSP_POD_IP";

	/**
	 * 镜像名称
	 */
	public final static String ENV_FCSP_IMAGE_NAME = "FCSP_IMAGE_NAME";

	/**
	 * 镜像版本号
	 */
	public final static String ENV_FCSP_IMAGE_TAG = "FCSP_IMAGE_TAG";

	//
	// /**
	// * 镜像ID
	// */
	// public final static String ENV_FCSP_IMAGE_ID = "FCSP_IMAGE_ID";

	/**
	 * etc文件夹
	 */
	public final static String PATH_DIR_ETC = "etc";

	/**
	 * 宿主机node的相关配置
	 */
	public final static String PATH_FILE_NODE = "node.properties";

	/**
	 * krb5配置文件
	 */
	public final static String PROPERTIES_NODE_KEY_PATH_FILE_KRB5CONF = "krb5.conf";

	/**
	 * hadoop keytab的login信息
	 */
	public final static String PROPERTIES_NODE_KEY_HADOOP_KEYTAB_LOGIN = "hbase.keytab.login";

	/**
	 * hadoop keytab的keytab路径
	 */
	public final static String PROPERTIES_NODE_KEY_HADOOP_KEYTAB_FIILE = "hbase.keytab.file";

	/**
	 * hadoop 而配置文件路径
	 */
	public final static String PROPERTIES_NODE_KEY_HADOOP_CONF_PATH = "hadoop.conf";

	/**
	 * hbase 而配置文件路径
	 */
	public final static String PROPERTIES_NODE_KEY_HBASE_CONF_PATH = "hbase.conf";

	/**
	 * hadoop etc配置目录地址
	 */
	public final static String PROPERTIES_NODE_KEY_HDFS_CONF_ETC_PATH = "hdfs.conf.etc";

	/**
	 * zookeeper 连接地址
	 */
	public final static String PROPERTIES_SYSTEM_KEY_ZOOKEEPER_CONF_ADDRESS = "zookeeper.conf.address";

	/**
	 * zookeeper 微服务权限key
	 */
	public final static String ZOOKEEPER_KEY_SECURITY = "/microsecurity";

	/**
	 * hbase 表空间
	 */
	public final static String PROPERTIES_SYSTEM_KEY_HBASE_NAMESPACE = "hbase.namespace.microservice";

	//
	// /**
	// * hadoop配置路径
	// */
	// public final static String PATH_DIR_HADOOP = "hadoop";
	//
	// /**
	// * hbase配置路径
	// */
	// public final static String PATH_DIR_HBASE = "hbase";
}
