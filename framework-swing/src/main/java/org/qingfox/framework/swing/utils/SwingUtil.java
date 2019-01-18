package org.qingfox.framework.swing.utils;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

public class SwingUtil {
	public final static int BEAUTYEYE = 0;

	public static String[] DEFAULT_FONT = new String[] { "Table.font",
			"TableHeader.font", "CheckBox.font", "Tree.font", "Viewport.font",
			"ProgressBar.font", "RadioButtonMenuItem.font", "ToolBar.font",
			"ColorChooser.font", "ToggleButton.font", "Panel.font",
			"TextArea.font", "Menu.font", "TableHeader.font", "TextField.font",
			"OptionPane.font", "MenuBar.font", "Button.font", "Label.font",
			"PasswordField.font", "ScrollPane.font", "MenuItem.font",
			"ToolTip.font", "List.font", "EditorPane.font", "Table.font",
			"TabbedPane.font", "RadioButton.font", "CheckBoxMenuItem.font",
			"TextPane.font", "PopupMenu.font", "TitledBorder.font",
			"ComboBox.font" };

	public static void setFont(Font font) {
		for (int i = 0; i < DEFAULT_FONT.length; i++)
			UIManager.put(DEFAULT_FONT[i], font);
	}

	public static JLabel createSpaceLabel(int width, int height) {
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(width, height));
		return label;
	}

	public static JLabel createSpaceLabel() {
		return new JLabel();
	}

	public static JLabel createSpaceLabel(String name) {
		return new JLabel(name, JLabel.CENTER);

	}

	public static void setStyle(int style) {
		switch (style) {
		case BEAUTYEYE:
			try {
				org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper
						.launchBeautyEyeLNF();
				BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
				org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper
						.launchBeautyEyeLNF();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
