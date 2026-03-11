package meldexun.betterconfig.gui;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.reflect.TypeUtils;

import meldexun.betterconfig.ConfigElementMetadata;
import meldexun.betterconfig.TypeAdapters;
import meldexun.betterconfig.TypeUtil;
import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;
import net.minecraftforge.fml.client.config.GuiEditArrayEntries.IArrayEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

class DummyConfigElement implements IConfigElement {

	private final ConfigElementMetadata metadata;
	private final Type type;
	private final Object defaultValue;
	private final ConfigGuiType configType;
	private final boolean isList;

	DummyConfigElement(ConfigElementMetadata metadata, Type type) {
		this(metadata, type, metadata.defaultValue());
	}

	DummyConfigElement(ConfigElementMetadata metadata, Type type, Object defaultValue) {
		this.metadata = metadata;
		this.type = type;
		this.defaultValue = defaultValue;
		if (TypeAdapters.hasAdapter(type)) {
			this.isList = false;
			if (TypeUtils.isAssignable(type, boolean.class)) {
				this.configType = ConfigGuiType.BOOLEAN;
			} else if (TypeUtils.isAssignable(type, byte.class) || TypeUtils.isAssignable(type, short.class) || TypeUtils.isAssignable(type, int.class) || TypeUtils.isAssignable(type, long.class) || TypeUtils.isAssignable(type, char.class)) {
				this.configType = ConfigGuiType.INTEGER;
			} else if (TypeUtils.isAssignable(type, float.class) || TypeUtils.isAssignable(type, double.class)) {
				this.configType = ConfigGuiType.DOUBLE;
			} else {
				this.configType = ConfigGuiType.STRING;
			}
		} else if (TypeUtil.isArrayOrCollection(type)) {
			this.isList = true;
			Type lowestType = type;
			while (TypeUtil.isArrayOrCollection(lowestType)) {
				lowestType = TypeUtil.getComponentOrElementType(lowestType);
			}
			if (TypeAdapters.hasAdapter(lowestType)) {
				if (TypeUtils.isAssignable(lowestType, boolean.class)) {
					this.configType = ConfigGuiType.BOOLEAN;
				} else if (TypeUtils.isAssignable(lowestType, byte.class) || TypeUtils.isAssignable(lowestType, short.class) || TypeUtils.isAssignable(lowestType, int.class) || TypeUtils.isAssignable(lowestType, long.class) || TypeUtils.isAssignable(lowestType, char.class)) {
					this.configType = ConfigGuiType.INTEGER;
				} else if (TypeUtils.isAssignable(lowestType, float.class) || TypeUtils.isAssignable(lowestType, double.class)) {
					this.configType = ConfigGuiType.DOUBLE;
				} else {
					this.configType = ConfigGuiType.STRING;
				}
			} else {
				this.configType = ConfigGuiType.CONFIG_CATEGORY;
			}
		} else {
			this.isList = false;
			this.configType = ConfigGuiType.CONFIG_CATEGORY;
		}
	}

	@Override
	public boolean isProperty() {
		return true;
	}

	@Override
	public String getName() {
		return this.metadata.name();
	}

	@Override
	public String getQualifiedName() {
		return this.metadata.name();
	}

	@Override
	public String getLanguageKey() {
		return this.metadata.hasLangKey() ? this.metadata.langKey() : this.metadata.name();
	}

	@Override
	public String getComment() {
		return this.metadata.hasComment() ? this.metadata.comment() : null;
	}

	@Override
	public ConfigGuiType getType() {
		return this.configType;
	}

	@Override
	public boolean isList() {
		return this.isList;
	}

	@Override
	public boolean requiresWorldRestart() {
		return this.metadata.requiresWorldRestart();
	}

	@Override
	public boolean requiresMcRestart() {
		return this.metadata.requiresMcRestart();
	}

	@Override
	public boolean showInGui() {
		return true;
	}

	@Override
	public boolean isListLengthFixed() {
		return !this.metadata.modifiable();
	}

	@Override
	public int getMaxListLength() {
		return -1;
	}

	@Deprecated
	@Override
	public Class<? extends IConfigEntry> getConfigEntryClass() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public Class<? extends IArrayEntry> getArrayEntryClass() {
		return null;
	}

	@Deprecated
	@Override
	public List<IConfigElement> getChildElements() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public boolean isDefault() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public Object getDefault() {
		return TypeUtil.toString(this.type, this.defaultValue);
	}

	@Deprecated
	@Override
	public Object[] getDefaults() {
		return null;
	}

	@Deprecated
	@Override
	public void setToDefault() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public Object get() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public Object[] getList() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void set(Object value) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void set(Object[] aVal) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public String[] getValidValues() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public Object getMinValue() {
		if (this.metadata.hasLongRange())
			return this.metadata.minLong();
		if (this.metadata.hasDoubleRange())
			return this.metadata.minDouble();
		if (this.getType() == ConfigGuiType.INTEGER)
			return Integer.MIN_VALUE;
		if (this.getType() == ConfigGuiType.DOUBLE)
			return -Double.MAX_VALUE;
		return null;
	}

	@Deprecated
	@Override
	public Object getMaxValue() {
		if (this.metadata.hasLongRange())
			return this.metadata.maxLong();
		if (this.metadata.hasDoubleRange())
			return this.metadata.maxDouble();
		if (this.getType() == ConfigGuiType.INTEGER)
			return Integer.MAX_VALUE;
		if (this.getType() == ConfigGuiType.DOUBLE)
			return Double.MAX_VALUE;
		return null;
	}

	@Deprecated
	@Override
	public Pattern getValidationPattern() {
		return null;
	}

}
