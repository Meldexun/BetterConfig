package meldexun.betterconfig.api.tree;

public interface IConfigValue<T extends IConfigContext<T>> extends IConfigElement<T> {

	String getValue();

	void setValue(String value);

}
