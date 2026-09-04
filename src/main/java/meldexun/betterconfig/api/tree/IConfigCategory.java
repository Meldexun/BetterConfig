package meldexun.betterconfig.api.tree;

import java.util.Map;

public interface IConfigCategory<T extends IConfigContext<T>> {

	Map<String, IConfigCategory<T>> getSubCategories();

	Map<String, IConfigElement<T>> getElements();

}
