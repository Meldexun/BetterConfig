package meldexun.betterconfig.api;

import java.util.Objects;

import meldexun.betterconfig.api.tree.IConfigCategory;
import meldexun.betterconfig.api.tree.IConfigContext;
import meldexun.betterconfig.api.tree.IConfigElement;

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
		if (dst.getElements().containsKey(dstName)) {
			throw new IllegalArgumentException("Can't move element because an element with name '" + dstName + "' already exists in the destination category");
		}

		IConfigElement<T> element = src.getElements().remove(srcName);
		dst.getElements().put(dstName, element);
		return element;
	}

}
