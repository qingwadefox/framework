package org.qingfox.framework.database.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.qingfox.framework.database.beans.ValueBean;
import org.qingfox.framework.database.request.base.BaseRequest;

public class MapRequest extends BaseRequest<Map<String, ValueBean>> {
	private static final long serialVersionUID = -7850493999583172111L;

	private List<Map<String, Object>> datas;

	public List<Map<String, Object>> getDatas() {
		return datas;
	}

	public void setDatas(List<Map<String, Object>> datas) {
		this.datas = datas;
	}

	public void addData(Map<String, Object> data) {
		if (this.datas == null) {
			this.datas = new ArrayList<Map<String, Object>>();
		}
		this.datas.add(data);
	}

}
