package meldexun.betterconfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import javax.annotation.Nullable;

import meldexun.betterconfig.api.BetterConfig;
import meldexun.betterconfig.api.tree.IConfigValue;

class ConfigValue extends ConfigElement implements IConfigValue<ConfigCategory> {

	private String value = "";

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	Type defaultType() {
		return String.class;
	}

	@Override
	boolean isConfigTypeEqual(Type type) {
		return ConfigUtil.isValue(type);
	}

	@Override
	void read(ConfigReader reader) throws IOException {
		this.value = reader.readRawLine();
	}

	@Override
	void write(ConfigWriter writer, BetterConfig settings, @Nullable Type type, @Nullable ConfigElementMetadata metadata, @Nullable Object instance) throws IOException {
		if (type == null) {
			// deprecated entry
			writer.write(this.value);
			return;
		}

		Objects.requireNonNull(type);
		Objects.requireNonNull(instance);
		if (!ConfigUtil.isValue(type)) {
			throw new IllegalArgumentException();
		}

		writer.write(this.value);
	}

	@Override
	void saveToConfig(BetterConfig settings, Type type, @Nullable ConfigElementMetadata metadata, @Nullable Object instance) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(instance);
		if (!ConfigUtil.isValue(type)) {
			throw new IllegalArgumentException();
		}

		this.value = TypeAdapters.get(type).serialize(instance);
	}

	@Override
	Object loadFromConfig(BetterConfig settings, Type type, @Nullable ConfigElementMetadata metadata, @Nullable Object instance) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(instance);
		if (!ConfigUtil.isValue(type)) {
			throw new IllegalArgumentException();
		}

		return TypeAdapters.get(type).deserialize(this.value);
	}

}
