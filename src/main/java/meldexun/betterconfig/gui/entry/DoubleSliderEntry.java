package meldexun.betterconfig.gui.entry;

import java.lang.reflect.Type;

import org.apache.commons.lang3.reflect.TypeUtils;

import meldexun.betterconfig.gui.EntryInfo;
import net.minecraftforge.fml.client.config.GuiSlider;

public class DoubleSliderEntry extends ButtonEntry<GuiSlider> {

	public DoubleSliderEntry(EntryInfo info, Type type, Object beforeValue) {
		super(info, type, beforeValue);
	}

	@Override
	protected GuiSlider createButton() {
		return new GuiSlider(0, 0, 0, 200, 18, "", "", this.info.minDouble(), this.info.maxDouble(), ((Number) this.beforeValue).doubleValue(), true, true);
	}

	@Override
	public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		return mouseEvent == 0 && super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
	}

	@Override
	protected boolean valueButtonLeftClicked() {
		return true;
	}

	@Override
	protected void setValue(Object value) {
		this.valueButton.setValue(((Number) value).doubleValue());
		this.valueButton.updateSlider();
	}

	@Override
	public Object getValue() {
		if (TypeUtils.isAssignable(this.type, float.class)) {
			return (float) this.valueButton.getValue();
		} else if (TypeUtils.isAssignable(this.type, double.class)) {
			return (double) this.valueButton.getValue();
		}
		throw new IllegalStateException();
	}

}
