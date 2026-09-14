package meldexun.betterconfig.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a field whose type is some kind of array, collection, or map to make this config option unmodifiable through the ingame config GUI. The
 * user will not be able to add or remove entries using the ingame config GUI but the entries themselves are still modifiable.<br>
 * <br>
 * This does <i>not</i> prevent or detect additions/removals/reordering of entries by editing the config file!
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Unmodifiable {

}
