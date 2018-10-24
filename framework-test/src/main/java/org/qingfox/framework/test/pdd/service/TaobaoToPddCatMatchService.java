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
package org.qingfox.framework.test.pdd.service;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.qingfox.framework.test.pdd.entity.CatEntity;
import org.qingfox.framework.test.pdd.service.inf.ICatMatchService;

import com.alibaba.fastjson.JSONObject;
import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.common.utils.FileUtil;
import com.framework.common.utils.ReadLine;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2018年1月5日
 * @功能说明：
 * 
 */
public class TaobaoToPddCatMatchService implements ICatMatchService {
	private static final ILogger logger = LoggerFactory.getLogger(TaobaoToPddCatMatchService.class);

	private String[] catExcludeWords;
	public Map<String, String[]> catIncludeWords;
	private Map<String, CatEntity> pddLv2Map;
	private Map<String, CatEntity> pddLv3Map;
	private Map<String, CatEntity> taobaoLv3Map;
	private Map<String, CatEntity> taobaoLv4Map;

	public TaobaoToPddCatMatchService(String configDir) throws Exception {

		String catMatchTaobaoDir = configDir + File.separator + "cat_match_taobao";

		try {
			catExcludeWords = FileUtils.readFileToString(new File(catMatchTaobaoDir + File.separator + "cat_exclude_words.dat")).split("\\|");
		} catch (Exception e) {
			logger.warn(e, "读取排除语义失败");
			catExcludeWords = new String[0];
		}

		try {
			catIncludeWords = new HashMap<String, String[]>();
			List<String> lines = FileUtils.readLines(new File(catMatchTaobaoDir + File.separator + "cat_include_words.dat"));
			for (String line : lines) {
				String[] _lines = line.split("=");
				catIncludeWords.put(_lines[0], _lines[1].split("\\|"));
			}
		} catch (Exception e) {
			logger.warn(e, "读取包含语义失败");
			catIncludeWords = new HashMap<String, String[]>();
		}
		pddLv2Map = new HashMap<String, CatEntity>();
		pddLv3Map = new HashMap<String, CatEntity>();
		taobaoLv3Map = new HashMap<String, CatEntity>();
		taobaoLv4Map = new HashMap<String, CatEntity>();
		FileUtil.readLine(new File(catMatchTaobaoDir + File.separator + "pdd_cat.dat"), new ReadLine() {
			@Override
			public void nextLine(String line, int number) {
				CatEntity cat = JSONObject.parseObject(line, CatEntity.class);
				switch (cat.getLv()) {
				case "2":
					pddLv2Map.put(cat.getId(), cat);
					break;
				case "3":
					switch (cat.getPid()) {
					case "897":
					case "898":
						break;
					default:
						pddLv3Map.put(cat.getId(), cat);
						break;
					}
					break;
				}
			}
		});

		FileUtil.readLine(new File(catMatchTaobaoDir + File.separator + "taobao_cat.dat"), new ReadLine() {
			@Override
			public void nextLine(String line, int number) {
				CatEntity cat = JSONObject.parseObject(line, CatEntity.class);
				switch (cat.getLv()) {
				case "3":
					taobaoLv3Map.put(cat.getId(), cat);
					break;
				case "4":
					taobaoLv4Map.put(cat.getId(), cat);
					break;
				}
			}
		});
	}

	@Override
	public String getCatId(String sellRootCatId, String buyCatId, String goodsName) {
		String taobaoCatName = goodsName;
		CatEntity taobaoCat = taobaoLv3Map.get(buyCatId);
		if (taobaoCat != null) {
			taobaoCatName = taobaoCat.getName();
		} else {
			taobaoCat = taobaoLv4Map.get(buyCatId);
			if (taobaoCat != null) {
				taobaoCat = taobaoLv3Map.get(taobaoCat.getPid());
				if (taobaoCat != null) {
					taobaoCatName = taobaoCat.getName();
				}
			}
		}

		String catId = getCatId(sellRootCatId, taobaoCatName, pddLv3Map.values());

		if (StringUtils.isEmpty(catId)) {
			catId = getCatId(sellRootCatId, taobaoCatName, pddLv2Map.values());
			if (StringUtils.isNotEmpty(catId)) {
				for (CatEntity pddCat : pddLv3Map.values()) {
					if (pddCat.getPid().equals(catId)) {
						catId = pddCat.getId();
						break;
					}
				}
			}
		}

		if (StringUtils.isEmpty(catId)) {
			catId = getCatId(sellRootCatId, goodsName, pddLv3Map.values());
			if (StringUtils.isEmpty(catId)) {
				catId = getCatId(sellRootCatId, goodsName, pddLv2Map.values());
				if (StringUtils.isNotEmpty(catId)) {
					for (CatEntity pddCat : pddLv3Map.values()) {
						if (pddCat.getPid().equals(catId)) {
							catId = pddCat.getId();
							break;
						}
					}
				}
			}

		}

		if (StringUtils.isEmpty(catId)) {
			for (CatEntity pddCat : pddLv3Map.values()) {
				if (pddCat.getRootId().equals(sellRootCatId)) {
					catId = pddCat.getId();
					break;
				}
			}
		}

		return catId;
	}

	private String getCatId(String sellRootCatId, String text, Collection<CatEntity> cats) {
		List<Term> Terms = HanLP.segment(text);

		for (CatEntity pddCat : pddLv3Map.values()) {
			if (!sellRootCatId.equals(pddCat.getRootId())) {
				continue;
			}
			String pddCatName = pddCat.getName();

			if (text.equals(pddCatName)) {
				return pddCat.getId();
			}

			for (Term term : Terms) {
				if (ArrayUtils.indexOf(catExcludeWords, term.word) != -1) {
					continue;
				}
				String[] termWords = null;
				if (catIncludeWords.containsKey(term.word)) {
					termWords = catIncludeWords.get(term.word);
				} else {
					termWords = new String[] { term.word };
				}
				for (String termWord : termWords) {
					if (pddCat.getName().indexOf(termWord) != -1) {
						return pddCat.getId();
					}
				}
			}
		}

		return null;
	}

}
