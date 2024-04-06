package meldexun.betterconfig.gui.entry;

import java.lang.reflect.Type;
import java.util.function.Supplier;

import meldexun.betterconfig.TypeUtil;
import meldexun.betterconfig.gui.ConfigGui;
import meldexun.betterconfig.gui.ConfigListGui;
import meldexun.betterconfig.gui.EntryInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class ListEntry extends ButtonEntry<GuiButtonExt> {

	protected final ConfigListGui childScreen;

	public <T extends GuiScreen & ConfigGui> ListEntry(T owningScreen, Supplier<String> childScreenTitle, EntryInfo info, Type type, Object defaultValue, Object beforeValue) {
		super(info, type, beforeValue);

		this.childScreen = new ConfigListGui(owningScreen, childScreenTitle, this.info, this.type, defaultValue, this.beforeValue) {
			@Override
			public void onGuiClosed() {
				super.onGuiClosed();
				ListEntry.this.valueButton.displayString = TypeUtil.toString(ListEntry.this.type, ListEntry.this.getValue());
				owningScreen.recalculateState();
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				ListEntry.this.valueButton.displayString = TypeUtil.toString(ListEntry.this.type, ListEntry.this.getValue());
				owningScreen.recalculateState();
			}
		};
	}

	@Override
	protected GuiButtonExt createButton() {
		return createDefaultButton(TypeUtil.toString(this.type, this.beforeValue));
	}

	@Override
	protected boolean valueButtonLeftClicked() {
		this.mc.displayGuiScreen(this.childScreen);
		return true;
	}

	@Override
	protected void setValue(Object value) {
		this.childScreen.setValue(value);
	}

	@Override
	public Object getValue() {
		return this.childScreen.getValue();
	}

}
