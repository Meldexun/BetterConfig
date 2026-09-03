package meldexun.betterconfig.api;

import javax.annotation.Nullable;

import meldexun.betterconfig.api.tree.IConfigCategory;
import meldexun.betterconfig.api.tree.IConfigContext;
import meldexun.betterconfig.api.tree.IConfigElement;

/**
 * Rename, move or delete categories or elements of a (sub-)category
 */
public class ConfigMigrationHelper {

	@Nullable
	public static <T extends IConfigContext<T>> IConfigCategory<T> renameCategory(IConfigCategory<T> parent, String oldName, String newName) {
		if (parent.getSubCategories().containsKey(newName)){
			throw new IllegalArgumentException("Failed to rename category, a category with this name already exists: " + newName);
		}

		IConfigCategory<T> category = parent.getSubCategories().remove(oldName);
		if (category != null) {
			parent.getSubCategories().put(newName, category);
		}
		return category;
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigCategory<T> moveCategory(String name, IConfigCategory<T> source, IConfigCategory<T> target) {
		if (target.getSubCategories().containsKey(name)){
			throw new IllegalArgumentException("Failed to move category, a category with this name already exists in the target: " + name);
		}

		IConfigCategory<T> category = source.getSubCategories().remove(name);
		if (category != null) {
			target.getSubCategories().put(name, category);
		}
		return category;
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigElement<T> renameElement(IConfigCategory<T> parent, String oldName, String newName) {
		if (parent.getElements().containsKey(newName)){
			throw new IllegalArgumentException("Failed to rename element, an element with this name already exists: " + newName);
		}

		IConfigElement<T> element = parent.getElements().remove(oldName);
		if (element != null) {
			parent.getElements().put(newName, element);
		}
		return element;
	}

	@Nullable
	public static <T extends IConfigContext<T>> IConfigElement<T> moveElement(String name, IConfigCategory<T> source, IConfigCategory<T> target) {
		if (target.getSubCategories().containsKey(name)){
			throw new IllegalArgumentException("Failed to move element, an element with this name already exists in the target: " + name);
		}

		IConfigElement<T> element = source.getElements().remove(name);
		if (element != null) {
			target.getElements().put(name, element);
		}
		return element;
	}

}
