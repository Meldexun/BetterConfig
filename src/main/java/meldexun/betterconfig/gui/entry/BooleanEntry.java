package meldexun.betterconfig.gui.entry;

import java.lang.reflect.Type;

import meldexun.betterconfig.gui.EntryInfo;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;

public class BooleanEntry extends ButtonEntry<GuiButtonExt> {

	public BooleanEntry(EntryInfo info, Type type, Object beforeValue) {
		super(info, type, beforeValue);
	}

	@Override
	protected GuiButtonExt createButton() {
		return createDefaultButton(Boolean.toString((boolean) this.beforeValue));
	}

	@Override
	protected boolean valueButtonLeftClicked() {
		this.setValue(!(boolean) this.getValue());
		return true;
	}

	@Override
	protected void setValue(Object value) {
		this.valueButton.displayString = Boolean.toString((boolean) value);
		this.valueButton.packedFGColour = (boolean) value ? GuiUtils.getColorCode('2', true) : GuiUtils.getColorCode('4', true);
	}

	@Override
	public Object getValue() {
		return Boolean.parseBoolean(this.valueButton.displayString);
	}

}
