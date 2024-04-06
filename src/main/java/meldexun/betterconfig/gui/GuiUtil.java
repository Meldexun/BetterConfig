package meldexun.betterconfig.gui;

import java.lang.reflect.Field;

import net.minecraftforge.fml.client.config.GuiEditArrayEntries;

public class GuiUtil {

	private static final Field controlWidth;
	static {
		try {
			controlWidth = GuiEditArrayEntries.class.getDeclaredField("controlWidth");
			controlWidth.setAccessible(true);
		} catch (ReflectiveOperationException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public static void setControlWidth(GuiEditArrayEntries entries, int controlWidth) {
		try {
			GuiUtil.controlWidth.setInt(entries, controlWidth);
		} catch (ReflectiveOperationException e) {
			throw new UnsupportedOperationException(e);
		}
	}

}
