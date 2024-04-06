package meldexun.betterconfig.gui;

import java.lang.reflect.Type;
import java.util.Optional;

import meldexun.betterconfig.IGuiListEntryExtended;
import meldexun.betterconfig.TypeUtil;
import meldexun.betterconfig.gui.entry.AbstractEntry;
import meldexun.betterconfig.gui.entry.StringEntry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiEditArrayEntries.BaseEntry;

public class ConfigListGuiEntry extends BaseEntry implements IGuiListEntryExtended, ListEntryBaseExt {

	protected final AbstractEntry entry;

	@SuppressWarnings("unchecked")
	public <T extends GuiScreen & ConfigGui> ConfigListGuiEntry(ConfigListGui.Entries owningEntryList, Type type, Optional<Object> value) {
		super(owningEntryList.owningScreen(), owningEntryList, owningEntryList.configElement);
		this.entry = AbstractEntry.create((T) this.owningScreen, () -> "[" + owningEntryList.listEntries.indexOf(this) + "]", owningEntryList.owningScreen().info(), type, TypeUtil.newInstance(type), value);
		this.isValidated = this.entry instanceof StringEntry && !type.equals(String.class);
	}

	@Override
	public void drawEntry(int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
		this.isValidValue = this.isValueSavable();
		super.drawEntry(index, x, y, width, height, mouseX, mouseY, isSelected, partialTicks);
		this.entry.drawEntry(index, width / 4, y, this.owningEntryList.controlWidth, height, mouseX, mouseY, isSelected, partialTicks);
	}

	@Override
	public void drawToolTip(int mouseX, int mouseY) {
		super.drawToolTip(mouseX, mouseY);
		this.entry.drawToolTip(mouseX, mouseY);
	}

	@Override
	public boolean mousePressedAll(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		if (this.entry.mousePressed(index, x, y, mouseEvent, relativeX, relativeY)) {
			return true;
		}
		return mouseEvent == 0 && this.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
	}

	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		super.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
		this.entry.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
	}

	@Override
	public void mouseClicked(int x, int y, int mouseEvent) {
		this.entry.mouseClicked(x, y, mouseEvent);
	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
		this.entry.keyTyped(eventChar, eventKey);
	}

	@Override
	public void updateCursorCounter() {
		this.entry.updateCursorCounter();
	}

	@Override
	public boolean isValueSavable() {
		return this.entry.isValueSavable();
	}

	@Override
	public Object getValue() {
		return this.entry.getValue();
	}

	@Override
	public int buttonOffsetY() {
		return -1;
	}

}
