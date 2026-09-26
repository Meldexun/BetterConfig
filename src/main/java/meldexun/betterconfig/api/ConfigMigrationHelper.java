package meldexun.betterconfig.api;

import java.util.Objects;
import java.util.function.Function;

import meldexun.betterconfig.api.tree.IConfigCategory;
import meldexun.betterconfig.api.tree.IConfigContext;
import meldexun.betterconfig.api.tree.IConfigElement;
import meldexun.betterconfig.api.tree.IConfigList;
import meldexun.betterconfig.api.tree.IConfigValue;

/**
 * Utility methods to make config migration easier.
 */
public class ConfigMigrationHelper {

	public static <T extends IConfigContext<T>> IConfigCategory<T> renameCategory(IConfigCategory<T> parent, String oldName, String newName) {
		return moveCategory(parent, oldName, parent, newName);
	}

	public static <T extends IConfigContext<T>> IConfigCategory<T> moveCategory(IConfigCategory<T> src, String name, IConfigCategory<T> dst) {
		return moveCategory(src, name, dst, name);
	}

	public static <T extends IConfigContext<T>> IConfigCategory<T> moveCategory(IConfigCategory<T> src, String srcName, IConfigCategory<T> dst, String dstName) {
		Objects.requireNonNull(src);
		Objects.requireNonNull(srcName);
		Objects.requireNonNull(dst);
		Objects.requireNonNull(dstName);
		if (!src.getSubCategories().containsKey(srcName)) {
			throw new IllegalArgumentException("Can't move element because an element with name '" + srcName + "' does not exist in the source category");
		}
		if (src == dst && srcName.equals(dstName)) {
			return src.getSubCategories().get(srcName);
		}
		if (dst.getSubCategories().containsKey(dstName)) {
			throw new IllegalArgumentException("Can't move element because an element with name '" + dstName + "' already exists in the destination category");
		}

		IConfigCategory<T> category = src.getSubCategories().remove(srcName);
		dst.getSubCategories().put(dstName, category);
		return category;
	}

	public static <T extends IConfigContext<T>> IConfigElement<T> renameElement(IConfigCategory<T> parent, String oldName, String newName) {
		return moveElement(parent, oldName, parent, newName);
	}

	public static <T extends IConfigContext<T>> IConfigElement<T> moveElement(IConfigCategory<T> src, String name, IConfigCategory<T> dst) {
		return moveElement(src, name, dst, name);
	}

	public static <T extends IConfigContext<T>> IConfigElement<T> moveElement(IConfigCategory<T> src, String srcName, IConfigCategory<T> dst, String dstName) {
		Objects.requireNonNull(src);
		Objects.requireNonNull(srcName);
		Objects.requireNonNull(dst);
		Objects.requireNonNull(dstName);
		if (!src.getElements().containsKey(srcName)) {
			throw new IllegalArgumentException("Can't move element because an element with name '" + srcName + "' does not exist in the source category");
		}
		if (src == dst && srcName.equals(dstName)) {
			return src.getElements().get(srcName);
		}
		if (dst.getElements().containsKey(dstName)) {
			throw new IllegalArgumentException("Can't move element because an element with name '" + dstName + "' already exists in the destination category");
		}

		IConfigElement<T> element = src.getElements().remove(srcName);
		dst.getElements().put(dstName, element);
		return element;
	}

	private static <T extends IConfigContext<T>> IConfigCategory<T> getNestedCategory(IConfigCategory<T> category, String... categoryNames) {
		for (String categoryName : categoryNames) {
			category = category.getSubCategories().get(categoryName);
			if(category == null) {
				throw new IllegalArgumentException("Can't get category because a category with name '" + categoryName + "' doesn't exists in the given nested path");
			}
		}
		return category;
	}

	/**
	 * Turns string lists with pattern 'key->separator->value' into a config map.
	 * Use the value transformer function to turn the value into any IConfigElement (a list, another map, etc.)
	 */
	private static <T extends IConfigContext<T>> IConfigCategory<T> transformStringListToMap(IConfigList<T> oldList, String separator, Function<String, IConfigElement<T>> valueTransformer, T context) {
		IConfigCategory<T> newMap = context.createCategory();

		for (IConfigElement<T> item : oldList.getList()) {
			if (!(item instanceof IConfigValue)){
				throw new IllegalArgumentException("Can't transform list to map because an element of the list was not primitive");
			}

			String line = ((IConfigValue<T>) item).getValue();
			// Skip empty lines
			if (line.trim().isEmpty()) {
				continue;
			}

			String[] parts = line.split(separator, 2);
			if (parts.length != 2) {
				throw new IllegalArgumentException("Can't transform list to map because an entry was not of pattern 'key"+separator+"value'");
			}

			String key = parts[0].trim();
			String value = parts[1].trim();

			newMap.getElements().put(key, valueTransformer.apply(value));
		}

		return newMap;
	}

	/**
	 * Turns string lists with pattern 'key->separator->value' into a IConfigCategory mapping the keys to their values
	 */
	private static <T extends  IConfigContext<T>> IConfigCategory<T> transformStringListToPrimitiveMap(IConfigList<T> oldList, String separator, T context) {
		return transformStringListToMap(oldList, separator, stringValue ->  {
			IConfigValue<T> value = context.createValue();
			value.setValue(stringValue);
			return value;
		}, context);
	}

}
