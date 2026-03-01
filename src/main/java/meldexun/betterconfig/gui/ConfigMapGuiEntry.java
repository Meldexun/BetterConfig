package meldexun.betterconfig.gui;

import java.lang.reflect.Type;
import java.util.Optional;

import meldexun.betterconfig.TypeUtil;
import meldexun.betterconfig.gui.entry.AbstractEntry;
import meldexun.betterconfig.gui.entry.StringEntry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiEditArrayEntries.BaseEntry;

public class ConfigMapGuiEntry extends BaseEntry implements IGuiListEntryExtended, ListEntryBaseExt {

	protected final AbstractEntry key;
	protected final AbstractEntry value;

	@SuppressWarnings("unchecked")
	public <T extends GuiScreen & ConfigGui> ConfigMapGuiEntry(ConfigMapGui.Entries owningEntryList, Type keyType, Type valueType, Optional<Object> beforeKey, Optional<Object> beforeValue) {
		super(owningEntryList.owningScreen(), owningEntryList, owningEntryList.configElement);
		this.key = AbstractEntry.create((T) this.owningScreen, () -> "[" + owningEntryList.listEntries.indexOf(this) + "]", owningEntryList.owningScreen().info(), keyType, TypeUtil.newInstance(keyType), beforeKey);
		this.value = AbstractEntry.create((T) this.owningScreen, () -> "[" + owningEntryList.listEntries.indexOf(this) + "]", owningEntryList.owningScreen().info(), valueType, TypeUtil.newInstance(valueType), beforeValue);
		this.isValidated = this.key instanceof StringEntry && !keyType.equals(String.class) || this.value instanceof StringEntry && !valueType.equals(String.class);
	}

	@Override
	public void drawEntry(int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
		this.isValidValue = this.isKeySavable() && this.isValueSavable();
		super.drawEntry(index, x, y, width, height, mouseX, mouseY, isSelected, partialTicks);
		this.key.drawEntry(index, width / 4, y, this.owningEntryList.controlWidth / 2, height, mouseX, mouseY, isSelected, partialTicks);
		this.value.drawEntry(index, width / 4 + this.owningEntryList.controlWidth / 2, y, this.owningEntryList.controlWidth / 2, height, mouseX, mouseY, isSelected, partialTicks);
	}

	@Override
	public void drawToolTip(int mouseX, int mouseY) {
		super.drawToolTip(mouseX, mouseY);
		this.key.drawToolTip(mouseX, mouseY);
		this.value.drawToolTip(mouseX, mouseY);
	}

	@Override
	public boolean mousePressedAll(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		if (this.key.mousePressed(index, x, y, mouseEvent, relativeX, relativeY)) {
			return true;
		}
		if (this.value.mousePressed(index, x, y, mouseEvent, relativeX, relativeY)) {
			return true;
		}
		return mouseEvent == 0 && this.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
	}

	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		super.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
		this.key.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
		this.value.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
	}

	@Override
	public void mouseClicked(int x, int y, int mouseEvent) {
		this.key.mouseClicked(x, y, mouseEvent);
		this.value.mouseClicked(x, y, mouseEvent);
	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
		this.key.keyTyped(eventChar, eventKey);
		this.value.keyTyped(eventChar, eventKey);
	}

	@Override
	public void updateCursorCounter() {
		this.key.updateCursorCounter();
		this.value.updateCursorCounter();
	}

	public boolean isKeySavable() {
		return this.key.isValueSavable();
	}

	public Object getKey() {
		return this.key.getValue();
	}

	@Override
	public boolean isValueSavable() {
		return this.value.isValueSavable();
	}

	@Override
	public Object getValue() {
		return this.value.getValue();
	}

	@Override
	public int buttonOffsetY() {
		return -1;
	}

}
