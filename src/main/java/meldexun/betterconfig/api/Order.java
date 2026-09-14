package meldexun.betterconfig.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import meldexun.betterconfig.api.BetterConfig.ConfigComparator;

/**
 * Annotate individual config settings with this annotation to provide an explicit ordering when using the {@link ConfigComparator#EXPLICIT EXPLICIT}
 * ordering in {@link BetterConfig#elementOrder()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Order {

	int value();

}
