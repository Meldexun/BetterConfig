package meldexun.betterconfig.gui.entry;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Predicate;

import meldexun.betterconfig.TypeAdapter;
import meldexun.betterconfig.TypeAdapters;
import meldexun.betterconfig.gui.EntryInfo;
import net.minecraft.client.gui.GuiTextField;

public class StringEntry extends AbstractEntry {

	protected final TypeAdapter<Object> typeAdapter;
	protected final GuiTextField textField;
	protected Predicate<String> validator;
	protected boolean isValid;

	public StringEntry(EntryInfo info, Type type, Object beforeValue) {
		super(info, type, beforeValue);
		this.typeAdapter = Objects.requireNonNull(TypeAdapters.get(this.type));
		this.validator = this.typeAdapter::isSerializedValue;
		this.textField = new GuiTextField(0, this.mc.fontRenderer, 0, 0, 200, 18) {
			@Override
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
				if (!enabled) {
					this.setFocused(false);
				}
			}
		};
		this.textField.setMaxStringLength(10000);
		this.setValue(beforeValue);
	}

	@Override
	public void drawEntry(int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
		this.textField.setEnabled(this.enabled());
		this.textField.x = x;
		this.textField.y = y;
		this.textField.width = width;
		this.textField.height = height;
		this.textField.drawTextBox();
	}

	@Override
	public void drawToolTip(int mouseX, int mouseY) {

	}

	@Override
	public boolean mousePressed(int index, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
		return false;
	}

	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {

	}

	@Override
	public void mouseClicked(int x, int y, int mouseEvent) {
		if (this.enabled()) {
			this.textField.mouseClicked(x, y, mouseEvent);
		}
	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
		if (this.enabled() && this.textField.textboxKeyTyped(eventChar, eventKey)) {
			this.isValid = this.validator.test(this.textField.getText());
		}
	}

	@Override
	public void updateCursorCounter() {
		if (this.enabled()) {
			this.textField.updateCursorCounter();
		}
	}

	@Override
	public boolean isValueSavable() {
		return this.isValid;
	}

	@Override
	protected void setValue(Object value) {
		this.textField.setText(this.typeAdapter.serialize(value));
		this.isValid = true;
	}

	@Override
	public Object getValue() {
		if (!this.isValid) {
			return this.beforeValue;
		}
		return this.typeAdapter.deserialize(this.textField.getText());
	}

	@Override
	public boolean isChanged() {
		return !this.isValid || super.isChanged();
	}

	@Override
	public boolean isDefault() {
		return this.isValid && super.isDefault();
	}

}
