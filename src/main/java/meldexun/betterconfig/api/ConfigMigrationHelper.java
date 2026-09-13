package meldexun.betterconfig.api;

import javax.annotation.Nullable;

import meldexun.betterconfig.api.tree.IConfigCategory;
import meldexun.betterconfig.api.tree.IConfigContext;
import meldexun.betterconfig.api.tree.IConfigElement;

/**
 * Utility methods to make config migration easier.
 */
public class ConfigMigrationHelper {

	@Nullable
	public static <T extends IConfigContext<T>> IConfigCategory<T> renameCategory(IConfigCategory<T> parent, String oldName, String newName) {
		return moveCategory(parent, oldName, parent, newName);
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigCategory<T> moveCategory(IConfigCategory<T> src, String name, IConfigCategory<T> dst) {
		return moveCategory(src, name, dst, name);
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigCategory<T> moveCategory(IConfigCategory<T> src, String srcName, IConfigCategory<T> dst, String dstName) {
		if (dst.getSubCategories().containsKey(dstName)) {
			throw new IllegalArgumentException("Failed to move category, a category with this name already exists in the target: " + dstName);
		}

		IConfigCategory<T> category = src.getSubCategories().remove(srcName);
		if (category != null) {
			dst.getSubCategories().put(dstName, category);
		}
		return category;
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigElement<T> renameElement(IConfigCategory<T> parent, String oldName, String newName) {
		return moveElement(parent, oldName, parent, newName);
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigElement<T> moveElement(IConfigCategory<T> src, String name, IConfigCategory<T> dst) {
		return moveElement(src, name, dst, name);
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigElement<T> moveElement(IConfigCategory<T> src, String srcName, IConfigCategory<T> dst, String dstName) {
		if (dst.getElements().containsKey(dstName)) {
			throw new IllegalArgumentException("Failed to move element, an element with this name already exists in the target: " + dstName);
		}

		IConfigElement<T> element = src.getElements().remove(srcName);
		if (element != null) {
			dst.getElements().put(dstName, element);
		}
		return element;
	}

}
