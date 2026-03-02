package meldexun.betterconfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import javax.annotation.Nullable;

import meldexun.betterconfig.gui.EntryInfo;

abstract class ConfigElement {

	final Config config;
	final DefaultSupplier<Type> type;
	@Nullable
	EntryInfo info;

	ConfigElement(Config config, DefaultSupplier<Type> type) {
		this.config = config;
		this.type = Objects.requireNonNull(type).copy();
	}

	static ConfigElement create(Config config, Type type) {
		return create(config, DefaultSupplier.of(type));
	}

	static ConfigElement create(Config config, DefaultSupplier<Type> type) {
		if (ConfigUtil.isValue(type.getOrDefault())) {
			return new ConfigValue(config, type);
		}
		if (ConfigUtil.isList(type.getOrDefault())) {
			return new ConfigList(config, type);
		}
		return new ConfigCategory(config, type);
	}

	boolean isConfigTypeEqual(Type type) {
		return ConfigUtil.isConfigTypeEqual(this.type.getOrDefault(), type);
	}

	abstract void read(ConfigReader reader) throws IOException;

	abstract void write(ConfigWriter writer) throws IOException;

	void loadInfo(Type type, EntryInfo info, @Nullable Object instance) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(info);
		if (!this.isConfigTypeEqual(type)) {
			throw new IllegalArgumentException();
		}
		this.type.set(type);
		this.info = info;
	}

	abstract void saveToConfig(Type type, @Nullable Object instance);

	abstract Object loadFromConfig(Type type, @Nullable Object instance);

	@Nullable
	EntryInfo info() {
		return info;
	}

}
