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
	public static <T extends IConfigContext<T>> IConfigCategory<T> moveCategory(IConfigCategory<T> source, String name, IConfigCategory<T> target) {
		return moveCategory(source, name, target, name);
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigCategory<T> moveCategory(IConfigCategory<T> source, String srcName, IConfigCategory<T> target, String dstName) {
		if (target.getSubCategories().containsKey(dstName)) {
			throw new IllegalArgumentException("Failed to move category, a category with this name already exists in the target: " + dstName);
		}

		IConfigCategory<T> category = source.getSubCategories().remove(srcName);
		if (category != null) {
			target.getSubCategories().put(dstName, category);
		}
		return category;
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigElement<T> renameElement(IConfigCategory<T> parent, String oldName, String newName) {
		return moveElement(parent, oldName, parent, newName);
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigElement<T> moveElement(IConfigCategory<T> source, String name, IConfigCategory<T> target) {
		return moveElement(source, name, target, name);
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigElement<T> moveElement(IConfigCategory<T> source, String srcName, IConfigCategory<T> target, String dstName) {
		if (target.getElements().containsKey(dstName)) {
			throw new IllegalArgumentException("Failed to move element, an element with this name already exists in the target: " + dstName);
		}

		IConfigElement<T> element = source.getElements().remove(srcName);
		if (element != null) {
			target.getElements().put(dstName, element);
		}
		return element;
	}

}
