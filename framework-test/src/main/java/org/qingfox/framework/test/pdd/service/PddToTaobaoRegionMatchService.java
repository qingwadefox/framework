/**
 * 
 */
package org.qingfox.framework.test.pdd.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.qingfox.framework.test.pdd.service.inf.IRegionMatchService;

import com.alibaba.fastjson.JSONObject;
import com.framework.common.exceptions.ServiceException;

/**
 * @author zhengwei
 *
 */
public class PddToTaobaoRegionMatchService implements IRegionMatchService {

	private Map<String, JSONObject> region;

	public PddToTaobaoRegionMatchService(String configDir) throws Exception {

		String regionMatchPddDir = configDir + File.separator + "region_match_pdd";
		File regionMatchPddFile = new File(regionMatchPddDir + File.separator + "region_match.dat");
		if (!regionMatchPddFile.exists()) {
			throw new ServiceException("文件【", regionMatchPddFile.getPath(), "】不存在");
		}

		region = new HashMap<String, JSONObject>();
		List<String> regionMatchPddLines = FileUtils.readLines(regionMatchPddFile);
		for (String regionMatchPddLine : regionMatchPddLines) {
			JSONObject matchRegionJson = JSONObject.parseObject(regionMatchPddLine);
			region.put(matchRegionJson.getString("sellRegionId"), matchRegionJson);
		}
	}

	@Override
	public String getRegionId(String sellRegionId) {
		return region.get(sellRegionId).getString("buyRegionId");
	}

}
