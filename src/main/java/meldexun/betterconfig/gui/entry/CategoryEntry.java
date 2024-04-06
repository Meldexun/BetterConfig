package meldexun.betterconfig.gui.entry;

import java.lang.reflect.Type;
import java.util.function.Supplier;

import meldexun.betterconfig.TypeUtil;
import meldexun.betterconfig.gui.ConfigCategoryGui;
import meldexun.betterconfig.gui.ConfigGui;
import meldexun.betterconfig.gui.EntryInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class CategoryEntry extends ButtonEntry<GuiButtonExt> {

	protected final ConfigCategoryGui childScreen;
	protected final Object instance;

	public <T extends GuiScreen & ConfigGui> CategoryEntry(T owningScreen, Supplier<String> childScreenTitle, EntryInfo info, Type type, Object beforeValue) {
		super(info, type, TypeUtil.copy(type, beforeValue));
		this.instance = beforeValue;

		this.childScreen = new ConfigCategoryGui(owningScreen, childScreenTitle, this.type, this.instance) {
			@Override
			public void onGuiClosed() {
				super.onGuiClosed();
				if (!(this.parentScreen instanceof ConfigCategoryGui)) {
					CategoryEntry.this.valueButton.displayString = TypeUtil.toString(CategoryEntry.this.type, CategoryEntry.this.getValue());
				}
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
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getValue() {
		return this.instance;
	}

	@Override
	public boolean isDefault() {
		return this.childScreen.entryList.areAllEntriesDefault(true);
	}

	@Override
	public void setToDefault() {
		if (this.enabled()) {
			this.childScreen.entryList.setAllToDefault(true);
		}
	}

	@Override
	public boolean isChanged() {
		return this.childScreen.entryList.hasChangedEntry(true);
	}

	@Override
	public void undoChanges() {
		if (this.enabled()) {
			this.childScreen.entryList.undoAllChanges(true);
		}
	}

	@Override
	public boolean saveChanges() {
		return this.childScreen.entryList.saveConfigElements() || this.info.requiresMcRestart();
	}

}
