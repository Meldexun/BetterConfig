package meldexun.betterconfig.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.common.config.Config.RangeInt;

/**
 * Long version of {@link RangeInt}. Guarantees that only values within the specified range get stored in the annotated long field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RangeLong {

	long min() default Long.MIN_VALUE;

	long max() default Long.MAX_VALUE;

}
