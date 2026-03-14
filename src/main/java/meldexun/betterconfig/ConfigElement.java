package meldexun.betterconfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import javax.annotation.Nullable;

import meldexun.betterconfig.api.BetterConfig;

abstract class ConfigElement {

	private final DefaultSupplier<Type> type;
	@Nullable
	private ConfigElementMetadata metadata;

	ConfigElement(DefaultSupplier<Type> type) {
		this.type = Objects.requireNonNull(type).copy();
	}

	static ConfigElement create(Type type) {
		return create(DefaultSupplier.of(type));
	}

	static ConfigElement create(DefaultSupplier<Type> type) {
		if (ConfigUtil.isValue(type.getOrDefault())) {
			return new ConfigValue(type);
		}
		if (ConfigUtil.isList(type.getOrDefault())) {
			return new ConfigList(type);
		}
		return new ConfigCategory(type);
	}

	boolean isConfigTypeEqual(Type type) {
		return ConfigUtil.isConfigTypeEqual(this.type.getOrDefault(), type);
	}

	abstract boolean isDefault();

	void clear() {
		this.type.reset();
		this.metadata = null;
	}

	abstract void read(ConfigReader reader) throws IOException;

	abstract void write(ConfigWriter writer, BetterConfig settings) throws IOException;

	void loadAnnotations(BetterConfig settings, Type type, ConfigElementMetadata metadata, @Nullable Object instance) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(metadata);
		if (!this.isConfigTypeEqual(type)) {
			throw new IllegalArgumentException();
		}
		this.type.set(type);
		this.metadata = metadata;
	}

	abstract void saveToConfig(BetterConfig settings, Type type, @Nullable Object instance);

	abstract Object loadFromConfig(BetterConfig settings, Type type, @Nullable Object instance);

	DefaultSupplier<Type> type() {
		return type;
	}

	@Nullable
	ConfigElementMetadata metadata() {
		return metadata;
	}

}
