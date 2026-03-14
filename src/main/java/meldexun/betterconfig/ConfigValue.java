package meldexun.betterconfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import javax.annotation.Nullable;

import meldexun.betterconfig.api.BetterConfig;

class ConfigValue extends ConfigElement {

	private String value;

	ConfigValue(DefaultSupplier<Type> type) {
		super(type);
		if (!ConfigUtil.isValue(this.type().getOrDefault())) {
			throw new IllegalArgumentException();
		}
		this.value = TypeAdapters.get(this.type().getOrDefault()).defaultSerializedValue();
	}

	@Override
	boolean isDefault() {
		ConfigElementMetadata metadata = this.metadata();
		if (metadata == null) {
			return false;
		}
		return this.value.equals(TypeUtil.toString(this.type().getOrDefault(), metadata.defaultValue()));
	}

	@Override
	void clear() {
		super.clear();
		this.value = null;
	}

	@Override
	void read(ConfigReader reader) throws IOException {
		String value = reader.readLine();
		if (!TypeAdapters.get(this.type().getOrDefault()).isSerializedValue(value)) {
			throw new IllegalArgumentException();
		}
		this.value = value;
	}

	@Override
	void write(ConfigWriter writer, BetterConfig settings) throws IOException {
		writer.write(this.value);
	}

	@Override
	void saveToConfig(BetterConfig settings, Type type, @Nullable Object instance) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(instance);
		if (!ConfigUtil.isValue(type)) {
			throw new IllegalArgumentException();
		}
		this.type().set(type);
		this.value = TypeAdapters.get(type).serialize(instance);
	}

	@Override
	Object loadFromConfig(BetterConfig settings, Type type, @Nullable Object instance) {
		Objects.requireNonNull(type);
		if (!ConfigUtil.isValue(type) || this.type().existsAndNotEqual(type)) {
			return instance;
		}
		return TypeAdapters.get(type).deserialize(this.value);
	}

}
