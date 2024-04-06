package meldexun.betterconfig.gui.entry;

import java.lang.reflect.Type;

import meldexun.betterconfig.gui.EntryInfo;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public abstract class ButtonEntry<T extends GuiButton> extends AbstractEntry {

	protected final T valueButton;

	public ButtonEntry(EntryInfo info, Type type, Object beforeValue) {
		super(info, type, beforeValue);
		this.valueButton = this.createButton();
	}

	protected abstract T createButton();

	protected static GuiButtonExt createDefaultButton(String text) {
		return new GuiButtonExt(0, 0, 0, 200, 18, text);
	}

	public T getButton() {
		return this.valueButton;
	}

	@Override
	public void drawEntry(int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
		this.valueButton.enabled = this.enabled();
		this.valueButton.x = x - 2;
		this.valueButton.y = y - 1;
		this.valueButton.width = width + 4;
		this.valueButton.height = height + 2;
		this.valueButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
	}

	@Override
	public void drawToolTip(int mouseX, int mouseY) {

	}

	@Override
	public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		if (!this.enabled()) {
			return false;
		}
		if (!this.valueButton.mousePressed(this.mc, x, y)) {
			return false;
		}
		if (!this.valueButtonPressed(mouseEvent)) {
			return false;
		}
		this.valueButton.playPressSound(this.mc.getSoundHandler());
		return true;
	}

	protected boolean valueButtonPressed(int mouseEvent) {
		if (mouseEvent == 0) {
			return this.valueButtonLeftClicked();
		}
		if (mouseEvent == 1) {
			return this.valueButtonRightClicked();
		}
		return false;
	}

	protected abstract boolean valueButtonLeftClicked();

	protected boolean valueButtonRightClicked() {
		return false;
	}

	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		if (this.enabled()) {
			this.valueButton.mouseReleased(x, y);
		}
	}

	@Override
	public void mouseClicked(int x, int y, int mouseEvent) {

	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {

	}

	@Override
	public void updateCursorCounter() {

	}

	@Override
	public boolean isValueSavable() {
		return true;
	}

}
