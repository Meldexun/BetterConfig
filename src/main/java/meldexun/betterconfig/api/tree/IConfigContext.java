package meldexun.betterconfig.api.tree;

public interface IConfigContext<T extends IConfigContext<T>> {

	IConfigValue<T> createValue();

	IConfigList<T> createList();

	IConfigCategory<T> createCategory();

}
