package meldexun.betterconfig.gui.entry;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.reflect.TypeUtils;

import meldexun.betterconfig.TypeAdapters;
import meldexun.betterconfig.TypeUtil;
import meldexun.betterconfig.gui.ConfigGui;
import meldexun.betterconfig.gui.EntryInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public abstract class AbstractEntry {

	protected final Minecraft mc = Minecraft.getMinecraft();
	protected final EntryInfo info;
	protected final Type type;
	protected final Object beforeValue;

	public AbstractEntry(EntryInfo info, Type type, Object beforeValue) {
		this.info = info;
		this.type = type;
		this.beforeValue = beforeValue;
	}

	public static <T extends GuiScreen & ConfigGui> AbstractEntry create(T owningScreen, String childScreenTitle, EntryInfo info, Type type, Object defaultValue, Object beforeValue) {
		return create(owningScreen, () -> childScreenTitle, info, type, defaultValue, Optional.of(beforeValue));
	}

	public static <T extends GuiScreen & ConfigGui> AbstractEntry create(T owningScreen, Supplier<String> childScreenTitle, EntryInfo info, Type type, Object defaultValue, Optional<Object> beforeValue) {
		if (TypeUtil.isEnum(type)) {
			return new EnumEntry(info, type, beforeValue.orElse(TypeUtil.getEnumConstants(type)[0]));
		}
		if (TypeAdapters.hasAdapter(type)) {
			if (TypeUtils.isAssignable(type, boolean.class)) {
				return new BooleanEntry(info, type, beforeValue.orElse(TypeUtil.newInstance(type)));
			}
			if (TypeUtils.isAssignable(type, byte.class)
					|| TypeUtils.isAssignable(type, short.class)
					|| TypeUtils.isAssignable(type, int.class)
					|| TypeUtils.isAssignable(type, long.class)
					|| TypeUtils.isAssignable(type, char.class)) {
				if (info.slidingOption()) {
					return new LongSliderEntry(info, type, beforeValue.orElse(TypeUtil.newInstance(type)));
				}
				return new LongEntry(info, type, beforeValue.orElse(TypeUtil.newInstance(type)));
			}
			if (TypeUtils.isAssignable(type, float.class)
					|| TypeUtils.isAssignable(type, double.class)) {
				if (info.slidingOption()) {
					return new DoubleSliderEntry(info, type, beforeValue.orElse(TypeUtil.newInstance(type)));
				}
				return new DoubleEntry(info, type, beforeValue.orElse(TypeUtil.newInstance(type)));
			}
			return new StringEntry(info, type, beforeValue.orElse(TypeAdapters.get(type).defaultValue()));
		}
		if (TypeUtil.isArrayOrCollection(type)) {
			return new ListEntry(owningScreen, childScreenTitle, info, type, defaultValue, beforeValue.orElseGet(() -> TypeUtil.newInstance(type)));
		}
		if (TypeUtil.isMap(type)) {
			return new MapEntry(owningScreen, childScreenTitle, info, type, defaultValue, beforeValue.orElseGet(() -> TypeUtil.newInstance(type)));
		}
		return new CategoryEntry(owningScreen, childScreenTitle, info, type, beforeValue.orElseGet(() -> TypeUtil.newInstance(type)));
	}

	public abstract void drawEntry(int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean isSelected, float partialTicks);

	public abstract void drawToolTip(int mouseX, int mouseY);

	public abstract boolean mousePressed(int index, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY);

	public abstract void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY);

	public abstract void mouseClicked(int x, int y, int mouseEvent);

	public abstract void keyTyped(char eventChar, int eventKey);

	public abstract void updateCursorCounter();

	public abstract boolean isValueSavable();

	protected abstract void setValue(Object value);

	public abstract Object getValue();

	public boolean enabled() {
		return !this.info.requiresWorldRestart() || this.mc.world == null;
	}

	public boolean isDefault() {
		return TypeUtil.equals(this.type, this.getValue(), this.info.defaultValue());
	}

	public void setToDefault() {
		if (this.enabled()) {
			this.setValue(this.info.defaultValue());
		}
	}

	public boolean isChanged() {
		return !TypeUtil.equals(this.type, this.getValue(), this.beforeValue);
	}

	public void undoChanges() {
		if (this.enabled()) {
			this.setValue(this.beforeValue);
		}
	}

	public boolean saveChanges() {
		return this.info.requiresMcRestart();
	}

}
