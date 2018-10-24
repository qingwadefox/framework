/**
 * 
 */
package org.qingfox.framework.test.pdd.constant;

/**
 * @author zhengwei
 *
 */
public class XPathConstant {

	public static final String TAOBAO_INDEX_EL_USERNAME = "//*[@id='J_SiteNavLogin']/div[1]/div[2]/a[1]";

	public static final String TAOBAO_LOGIN_IMG_QRCODE = "//*[@id='J_QRCodeImg']/img";

	public static final String TAOBAO_LOGIN_EL_SWITCH = "//*[@id='J_Quick2Static']";

	public static final String TAOBAO_LOGIN_EL_USERNAME = "//*[@name='TPL_username']";

	public static final String TAOBAO_LOGIN_EL_PASSWORD = "//*[@name='TPL_password']";

	public static final String TAOBAO_LOGIN_BTN_SUBMIT = "//*[@id='J_SubmitStatic']";

	public static final String TAOBAO_ITEM_DETAILS_EL_SKU = "//li[@data-value='" + ParamConstant.DEFAULT_PARAM + "']";

	public static final String TAOBAO_ITEM_DETAILS_EL_BUY = "//*[@class='tb-btn-buy']/a";

	public static final String TAOBAO_ITEM_BUY_EL_NUMBER = "//div[@class='quantity-inner']/input";

	public static final String TAOBAO_ITEM_BUY_EL_DETAIL = "//div[@class='memo-detail']/textarea";

	public static final String TAOBAO_ITEM_BUY_EL_REALPAY = "//*[@id='realPay_1']/div/span[3]";

	public static final String TAOBAO_ITEM_BUY_EL_ADDRESSEDIT = "//*[@id='confirmOrder_1']/div[2]/div/ul/li/div/label/a";

	public static final String TAOBAO_ITEM_BUY_EL_ADDRESSEDITOTHER = "// *[@id='confirmOrder_1']/div[2]/div/ul/li[1]/div/div[2]/p/span/a";

	public static final String TAOBAO_ITEM_BUY_EL_ADDRESSEDITFRAME = "//iframe[@class='add-addr-iframe']";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_EDIT = "//*[@id='city-title']";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_PROVINCE = "//*[@attr-cont='city-province']";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_CITY = "//a[@attr-cont='city-city']";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_DISTRICT = "//a[@attr-cont='city-district']";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_DISTRICT_CONTENT = "//div[@class='city-select city-district']/dl[1]/dd";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_DISTRICT_FIRST = "//div[@class='city-select city-district']/dl[1]/dd/a[1]";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_VALUE = "//a[@attr-id='" + ParamConstant.DEFAULT_PARAM + "']";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_STREET = "//a[@attr-cont='city-street']";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_STREET_FIRST = "//div[contains(@class,'city-street')]/dl[1]/dd/a[1]";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_ADDRESS = "//*[@id='J_Street']";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_NAME = "//*[@name='fullName']";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_MOBILE = "//*[@name='mobile']";

	public static final String TAOBAO_ITEM_BUY_ADDRESSEDITIFRAME_BTN_SUBMIT = "//button[@data-phase-id='d_p_saveSubmit']";

	public static final String TAOBAO_ITEM_BUY_EL_LOCK = "//div[@id='nc_1_wrapper']";

	public static final String TAOBAO_LOGIN_EL_LOCK = "//*[@id='nc_1_n1z']";

	// 登陆按钮ID
	public static final String AMM_LOGIN_EL_LOGIN = "//*[@id='J_menu_login']";

	public static final String AMM_INDEX_EL_USERNAME = "//*[@id='J_menu_product']/div[1]/span";
	

	// 切换登陆方式
	public static final String AMM_LOGIN_EL_FRAME = "//iframe[@name='taobaoLoginIfr']";

	// 二维码图片元素XPATH
	public static final String AMM_LOGIN_EL_CODE = "//*[@id='J_QRCodeImg']/img";

	// 切换登陆方式用户名密码
	public static final String AMM_LOGIN_EL_SWITCHPW = "//*[@id='J_Quick2Static']";

	// 用户名ID
	public static final String AMM_LOGIN_EL_USERNAME = "//input[@id='TPL_username_1']";

	// 密码ID
	public static final String AMM_LOGIN_EL_PASSWORD = "//input[@id='TPL_password_1']";

	public static final String AMM_LOGIN_EL_LOCK = "//*[@id='nc_1_n1z']";

	// 密码ID
	public static final String AMM_LOGIN_EL_SUBMIT = "//*[@id='J_SubmitStatic']";

	// 商品详情细节数据divID
	public static final String AMM_ITEMDETAILS_GOODS_DIV = "//*[@id='J_Pine']";

	// 商品详情名称xpath
	public static final String AMM_ITEMDETAILS_GOODS_NAME = "//*[@id='J_Title']/h3";

	// 商品描述xpath
	public static final String AMM_ITEMDETAILS_GOODS_DESC = "//*[@id='J_Title']/p";

	// 商品详情原价元素name
	public static final String AMM_ITEMDETAILS_GOODS_CURRENTPRICE = "//form[@id='J_FrmBid']/input[@name='current_price']";

	public static final String AMM_TEMDETAILS_GOODS_SKU = "//*[@id='J_isku']/div//dl";

	public static final String AMM_TEMDETAILS_GOODS_SKU_PARENT = "dd/ul[@data-property]";

	public static final String AMM_TEMDETAILS_GOODS_SKU_ALL = "dd/ul[@data-property]//li";

	// 商品详情图片xpath
	public static final String AMM_ITEMDETAILS_DESCIMG = "//*[@id='J_DivItemDesc']//table[@background]";

	public static final String AMM_SEARCH_LIST = "//*[@id='J_search_results']/div/div";

	public static final String AMM_SEARCH_LIST_ID = "div[@class='pic-box']/a";

	public static final String AMM_SEARCH_LIST_COMM = "div[@class='box-content']/div[3]/span[2]/span[2]/span[2]";

	public static final String TAOBAO_ORDER_LIST_EL_DEST = "//*[@id='tp-bought-root']/div[starts-with(@class,'index-mod__order-container')]";

	// *[@id="tp-bought-root"]/div[7]/div/table/tbody[1]/tr/td[1]/label/span[2]
	public static final String TAOBAO_ORDER_LIST_DEST_TIME = "div/table/tbody[1]/tr/td[1]/label/span[2]";

	// *[@id="tp-bought-root"]/div[7]/div/table/tbody[1]/tr/td[1]/span/span[3]
	public static final String TAOBAO_ORDER_LIST_DEST_ID = "div/table/tbody[1]/tr/td[1]/span/span[3]";

	public static final String TAOBAO_ORDER_LIST_DEST_STATE = "div/table/tbody[2]/tr[1]/td[6]/div/p/span";

	// *[@id="tp-bought-root"]/div[5]/div/table/tbody[2]/tr[1]/td[5]/div/div[1]/p/strong/span[2]
	public static final String TAOBAO_ORDER_LIST_DEST_PRICE = "div/table/tbody[2]/tr[1]/td[5]/div/div[1]/p/strong/span[2]";

	public static final String TAOBAO_ORDER_LIST_NEXT = "//*[@id='tp-bought-root']//ul[@class='pagination ']/li[last()]";

}
