package meldexun.betterconfig;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeUtils;

import meldexun.betterconfig.api.BetterConfig;
import meldexun.betterconfig.api.tree.IConfigElement;
import meldexun.betterconfig.api.tree.IConfigList;

class ConfigList extends ConfigElement implements IConfigList<Config> {

	private final List<ConfigElement> list = new ArrayList<>();

	@Override
	@SuppressWarnings("unchecked")
	public List<IConfigElement<Config>> getList() {
		return (List<IConfigElement<Config>>) (List<?>) this.list;
	}

	@Override
	Type defaultType() {
		return TypeUtils.parameterize(List.class, TypeUtils.WILDCARD_ALL);
	}

	@Override
	boolean isConfigTypeEqual(Type type) {
		return ConfigUtil.isList(type);
	}

	@Override
	void read(ConfigReader reader) throws IOException, ConfigParseException {
		this.list.clear();

		int start = reader.lineNumber();
		if (!reader.readLineIfMatching(ConfigList::isListStart)) {
			throw new ConfigSyntaxException("Expected list start at line " + start);
		}

		while (true) {
			try {
				if (reader.readLineIfMatching(ConfigList::isListEnd)) {
					break;
				}
			} catch (EOFException e) {
				throw new ConfigSyntaxException("Missing list end for list starting at line " + start, e);
			}

			ConfigElement element;
			if (ConfigCategory.isCategoryStart(reader.peekLine())) {
				element = new ConfigCategory();
			} else if (ConfigList.isListStart(reader.peekLine())) {
				element = new ConfigList();
			} else {
				element = new ConfigValue();
				reader.stripStart(null);
			}
			element.read(reader);
			this.list.add(element);
		}
	}

	static boolean isListStart(String line) {
		return ConfigReader.strippedEquals(line, "<");
	}

	static boolean isListEnd(String line) {
		return ConfigReader.strippedEquals(line, ">");
	}

	@Override
	void write(ConfigWriter writer, BetterConfig settings, @Nullable Type type, @Nullable ConfigElementMetadata metadata, @Nullable Object instance) throws IOException {
		if (type == null) {
			// deprecated entry
			writer.writeLine('<');
			writer.incrementIndentation();
			for (ConfigElement child : this.list) {
				child.write(writer, settings, null, null, null);
				writer.newLine();
			}
			writer.decrementIndentation();
			writer.write('>');
			return;
		}

		Objects.requireNonNull(type);
		Objects.requireNonNull(instance);
		if (!ConfigUtil.isList(type)) {
			throw new IllegalArgumentException();
		}

		writer.writeLine('<');
		writer.incrementIndentation();
		Type elementType = TypeUtil.getComponentOrElementType(type);
		for (ConfigElement child : this.list) {
			child.write(writer, settings, elementType, null, TypeUtil.newInstance(elementType)); // TODO compute metadata for list elements?
			writer.newLine();
		}
		writer.decrementIndentation();
		writer.write('>');
	}

	@Override
	void saveToConfig(BetterConfig settings, Type type, @Nullable ConfigElementMetadata metadata, @Nullable Object instance) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(instance);
		if (!ConfigUtil.isList(type)) {
			throw new IllegalArgumentException();
		}

		if (TypeUtil.isArray(type)) {
			this.list.clear();
			Type componentType = TypeUtil.getComponentType(type);

			for (int i = 0; i < Array.getLength(instance); i++) {
				ConfigElement element = ConfigElement.create(componentType);
				element.saveToConfig(settings, componentType, null, Array.get(instance, i)); // TODO compute metadata for list elements?
				this.list.add(element);
			}
		} else if (TypeUtil.isCollection(type)) {
			this.list.clear();
			Type elementType = TypeUtil.getElementType(type);

			for (Object value : (Collection<?>) instance) {
				ConfigElement element = ConfigElement.create(elementType);
				element.saveToConfig(settings, elementType, null, value); // TODO compute metadata for list elements?
				this.list.add(element);
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	Object loadFromConfig(BetterConfig settings, Type type, @Nullable ConfigElementMetadata metadata, @Nullable Object instance) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(instance);
		if (!ConfigUtil.isList(type)) {
			throw new IllegalArgumentException();
		}

		if (TypeUtil.isArray(type)) {
			Type componentType = TypeUtil.getComponentType(type);

			Object array = Array.newInstance(TypeUtil.getRawType(componentType), this.list.size());
			for (int i = 0; i < this.list.size(); i++) {
				if (!this.list.get(i).isConfigTypeEqual(componentType)) {
					continue;
				}
				Array.set(array, i, this.list.get(i).loadFromConfig(settings, componentType, null, TypeUtil.newInstance(componentType))); // TODO compute metadata for list elements?
			}

			return array;
		} else if (TypeUtil.isCollection(type)) {
			Type elementType = TypeUtil.getElementType(type);

			Collection<Object> collection = (Collection<Object>) TypeUtil.newInstance(type, instance);
			for (ConfigElement value : this.list) {
				if (!value.isConfigTypeEqual(elementType)) {
					continue;
				}
				collection.add(value.loadFromConfig(settings, elementType, null, TypeUtil.newInstance(elementType))); // TODO compute metadata for list elements?
			}

			return collection;
		} else {
			throw new IllegalArgumentException();
		}
	}

}
