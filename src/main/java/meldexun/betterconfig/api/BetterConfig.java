package meldexun.betterconfig.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BetterConfig {

	String modid();

	String name() default "";

	String category() default "general";

	boolean lowerCaseCategories() default true;

	boolean bigCategoryComments() default true;

	boolean addRangesToComments() default true;

	boolean addDefaultsToComments() default true;

	boolean removeDeprecatedEntries() default false;

}
