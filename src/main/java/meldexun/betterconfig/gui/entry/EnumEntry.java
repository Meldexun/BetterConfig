package meldexun.betterconfig.gui.entry;

import java.lang.reflect.Type;

import org.apache.commons.lang3.ArrayUtils;

import meldexun.betterconfig.TypeUtil;
import meldexun.betterconfig.gui.EntryInfo;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class EnumEntry extends ButtonEntry<GuiButtonExt> {

	public EnumEntry(EntryInfo info, Type type, Object beforeValue) {
		super(info, type, beforeValue);
	}

	@Override
	protected GuiButtonExt createButton() {
		return createDefaultButton(((Enum<?>) this.beforeValue).name());
	}

	@Override
	protected boolean valueButtonLeftClicked() {
		Object[] values = TypeUtil.getEnumConstants(this.type);
		int index = ArrayUtils.indexOf(values, this.getValue());
		this.setValue(values[(index + 1) % values.length]);
		return true;
	}

	@Override
	protected boolean valueButtonRightClicked() {
		Object[] values = TypeUtil.getEnumConstants(this.type);
		int index = ArrayUtils.indexOf(values, this.getValue());
		this.setValue(values[(index + values.length - 1) % values.length]);
		return true;
	}

	@Override
	protected void setValue(Object value) {
		this.valueButton.displayString = ((Enum<?>) value).name();
	}

	@Override
	public Object getValue() {
		return TypeUtil.valueOf(this.type, this.valueButton.displayString);
	}

}
