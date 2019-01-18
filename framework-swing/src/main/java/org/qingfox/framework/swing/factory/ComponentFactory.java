package org.qingfox.framework.swing.factory;

import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.qingfox.framework.swing.components.IComponent;

public class ComponentFactory {

	private static Map<String, IComponent> baseFrameMaps;

	private static Boolean initStyle = false;

	private static Integer LABEL_SIZE = 100;

	private static Integer FIELDPANEL_HEIGH = 20;

	public static void init(IComponent baseFrame) {
		if (baseFrameMaps == null) {
			baseFrameMaps = new HashMap<String, IComponent>();
		}
		if (!initStyle) {
			try {
				org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
				BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
				org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();

				// 设置统一字体
				FontUIResource fontRes = new FontUIResource(new Font("微软雅黑", Font.PLAIN, 12));
				for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
					Object key = keys.nextElement();
					Object value = UIManager.get(key);
					if (value instanceof FontUIResource) {
						UIManager.put(key, fontRes);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				initStyle = true;
			}

			UIManager.put("Table.selectionBackground", new ColorUIResource(Color.LIGHT_GRAY));
		}

		baseFrameMaps.put(baseFrame.getCode(), baseFrame);
	}
	public static void setLabelSize(Integer labelSize) {
		ComponentFactory.LABEL_SIZE = labelSize;
	}

	public static Integer getLabelSize() {
		return LABEL_SIZE;
	}

	public static Integer getFieldPanelHeight() {
		return FIELDPANEL_HEIGH;
	}

	public static void setFieldPanelHeight(Integer fieldPanelHeight) {
		FIELDPANEL_HEIGH = fieldPanelHeight;
	}

	public static void setLABEL_SIZE(Integer lABEL_SIZE) {
		LABEL_SIZE = lABEL_SIZE;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getComponent(Class<T> componentClass, String code) {
		return (T) baseFrameMaps.get(code);
	}

}
