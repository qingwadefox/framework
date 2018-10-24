/**
 * 
 */
package com.framework.test.pdd.constant;

/**
 * @author zhengwei
 *
 */
public class URLConstant {

	// 拼多多订单详情查詢
	public static final String PDD_ORDER_DETAIL = "http://mms.pinduoduo.com/mars/shop/orderDetail";

	// 拼多多商品sn查詢
	public static final String PDD_GOODS_SN = "http://mms.pinduoduo.com/malls/" + ParamConstant.PDD_MALLID + "/goodsList?sort_by=id&sort_type=DESC&size=15&page=1&goods_id=";

	// 拼多多商品詳情查詢
	public static final String PDD_GOODS_DETAIL = "http://mms.pinduoduo.com/glide/v2/mms/query/commit/on_shop/detail";

	// 拼多多商品在售列表查詢
	public static final String PDD_GOODS_ONSELL_LIST = "http://mms.pinduoduo.com/malls/" + ParamConstant.PDD_MALLID + "/goodsList?sort_by=id&sort_type=DESC&is_onsale=1&sold_out=0&size=15&page=";

	// 拼多多订单列表查询
	public static final String PDD_ORDER_LIST = "http://mms.pinduoduo.com/mars/shop/orderList";

	// 淘宝客推广连接
	public static final String AMM_ITEM_BUY = "https://s.click.taobao.com/";

	// 淘宝商品购买
	public static final String TAOBAO_ITEM_BUY = "https://item.taobao.com/item.htm";

	// 淘宝登录
	public static final String TAOBAO_LOGIN = "https://login.taobao.com/member/login.jhtml";

	// 淘宝主页
	public static final String TAOBAO_INDEX = "https://www.taobao.com/";

	// 用户主页
	public static final String TAOBAO_MY = "https://i.taobao.com/my_taobao.htm";

	// 下单页面
	public static final String TAOBAO_BUY = "https://buy.taobao.com/auction/buy_now.jhtml";

	// 获取sku列表地址
	public static final String PDD_SKU_LIST = "http://mms.pinduoduo.com/glide/v2/mms/query/spec/name/list";

	// 获取sku详情地址
	public static final String PDD_SKU_DESC = "http://mms.pinduoduo.com/glide/v2/mms/query/spec/by/name";
	

	// 获取sku详情地址
	public static final String PDD_GOODS_OFFSALE = "https://mms.pinduoduo.com/glide/v2/mms/edit/switch/offSale";

	// 获取主营分类ID
	public static final String PDD_MAINCAT = "http://mms.pinduoduo.com/earth/api/mallInfo/catList";

	// 获取验证码url
	public static final String PDD_LOGIN_CODE = "http://mms.pinduoduo.com/captchaCode/getCaptchaCode?rd=820244";

	// 登陆url
	public static final String PDD_LOGIN = "http://mms.pinduoduo.com/auth";

	public static final String PDD_GOODS_CREATE = "http://mms.pinduoduo.com/glide/v2/mms/edit/commit/create_new";

	// 保存商品URL
	public static final String PDD_GOODS_UPDATE = "http://mms.pinduoduo.com/glide/v2/mms/edit/commit/update";

	public static final String PDD_GOODS_CREATE_IMAGE_UPLOAD = "http://mms.pinduoduo.com/earth/api/upload/getSignature";

	// 商品提交
	public static final String PDD_GOODS_CREATE_SUBMIT = "http://mms.pinduoduo.com/glide/v2/mms/edit/commit/submit";

	public static final String PDD_GOODS_COSTTEMPLATE = "http://mms.pinduoduo.com/express_base/cost_template/get_list";

	// 获取cat列表地址
	public static final String PDD_CAT_LIST = "http://mms.pinduoduo.com/vodka/v2/mms/categories?parentId=";

	public static final String PDD_ORDER_MOBILE = "http://mms.pinduoduo.com/mars/mobile/queryMobileByOrderSn";

	public static final String AMM_LOGIN = "https://www.alimama.com/index.htm";

	// 获取推广url
	public static final String AMM_POP = "http://pub.alimama.com/common/code/getAuctionCode.json?scenes=1";

	public static final String AMM_ITEMDETAILS = "https://item.taobao.com/item.htm";

	// 商品详情图片连接前缀
	public static final String AMM_ITEMDETAILS_IMG = "http://gd1.alicdn.com/imgextra/";

	public static final String TAOBAO_ORDER_LIST = "https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm";

	public static final String TAOBAO_ORDER_MSG = "https://buyertrade.taobao.com/trade/json/getMessage.htm?biz_order_id=" + ParamConstant.DEFAULT_PARAM + "&user_type=1&archive=false";

	public static final String TAOBAO_ORDER_LOGISTICS = "https://buyertrade.taobao.com/trade/json/transit_step.do?bizOrderId=" + ParamConstant.DEFAULT_PARAM;

	public static final String PDD_ORDER_SHIP = "http://mms.pinduoduo.com/express_base/shop/orders/shipping";

	public static final String PDD_CHAT = "http://mms.pinduoduo.com/chats/malls/" + ParamConstant.DEFAULT_PARAM + "/chats?version=2.2&log_id=";

	public static final String PDD_ORDER_UNGROUP = "http://mms.pinduoduo.com/chats/ungroupOrders?pageSize=20&pageNo=";

	public static final String TAOBAO_HEART = "https://i.taobao.com/my_taobao.htm";

	public static final String AMM_HEART = "http://pub.alimama.com/myunion.htm#!/report/detail/taoke";

	public static final String PDD_HEART = "http://mms.pinduoduo.com/Pdd.html#/myaccount/SafetyManage";

	public static final String PDD_CHAT_USER = "http://mms.pinduoduo.com/chats/users/";

	public static final String AMM_COMMISSION_SEARCH = "http://pub.alimama.com/promo/search/index.htm?q={0}&_t={1}";

}
