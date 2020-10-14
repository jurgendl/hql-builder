package org.tools.hqlbuilder.client;

import java.awt.Font;

import javax.swing.UIManager;

import org.tools.hqlbuilder.common.icons.CommonIcons;

public class DefaultFonts {
	public static void main(String[] args) {
		new DefaultFonts().init();
	}

	public void test() {
		//
	}

	public void init() {
		Font font = ClientUtils.getDefaultFont().deriveFont(16f);
		try {
			HqlBuilderAction fontAction = new HqlBuilderAction(null, this, "test", true, HqlBuilderFrameConstants.FONT,
					CommonIcons.getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.FONT),
					HqlBuilderFrameConstants.FONT, HqlBuilderFrameConstants.FONT, true, null, null,
					HqlBuilderFrameConstants.PERSISTENT_ID, Font.class, font);
			font = (Font) fontAction.getValue();
		} catch (Exception ex) {
			//
		}

		javax.swing.plaf.FontUIResource uif = new javax.swing.plaf.FontUIResource(font);
		javax.swing.plaf.FontUIResource uifs = new javax.swing.plaf.FontUIResource(
				font.deriveFont((float) font.getSize() - 2));

		java.util.Enumeration<?> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				System.out.println(key + "=" + value);
				javax.swing.plaf.FontUIResource f = (javax.swing.plaf.FontUIResource) value;
				if (f.getSize() < 12)
					UIManager.put(key, uifs);
				else
					UIManager.put(key, uif);
			}
		}

		System.out.println("-------------------------------------------");

		keys = UIManager.getLookAndFeelDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				System.out.println(key + "=" + value);
				javax.swing.plaf.FontUIResource f = (javax.swing.plaf.FontUIResource) value;
				if (f.getSize() < 12)
					UIManager.put(key, uifs);
				else
					UIManager.put(key, uif);
			}
		}
	}
}

