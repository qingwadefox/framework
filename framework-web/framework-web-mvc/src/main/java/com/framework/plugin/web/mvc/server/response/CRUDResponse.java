package com.framework.plugin.web.common.server.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.framework.plugin.web.common.bean.PageBean;
import com.framework.plugin.web.common.server.response.basic.BasicResponse;

public class CRUDResponse extends BasicResponse {

	private static final long serialVersionUID = 6465900401407159970L;

	private List<Map<String, String>> datas;

	private PageBean page;

	public List<Map<String, String>> getDatas() {
		return datas;
	}

	public CRUDResponse setDatas(List<Map<String, String>> datas) {
		this.datas = datas;
		return this;
	}

	public PageBean getPage() {
		return page;
	}

	public CRUDResponse setPage(PageBean page) {
		this.page = page;
		return this;
	}

	public CRUDResponse setData(Map<String, String> data) {
		datas = new ArrayList<Map<String, String>>();
		datas.add(data);
		return this;
	}

	public void print(HttpServletResponse response) {

		try {
			switch (this.getMode()) {
			case 1:
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				response.getWriter().write(JSONObject.toJSONString(this));
				break;
			case 2:
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/java; charset=utf-8");
				OutputStream outputStream = response.getOutputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(this);
				outputStream.write(baos.toByteArray());
				outputStream.flush();
				outputStream.close();
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
