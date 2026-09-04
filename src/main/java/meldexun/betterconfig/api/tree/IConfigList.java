package meldexun.betterconfig.api.tree;

import java.util.List;

public interface IConfigList<T extends IConfigContext<T>> extends IConfigElement<T> {

	List<IConfigElement<T>> getList();

}
