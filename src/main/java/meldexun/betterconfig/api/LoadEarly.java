package meldexun.betterconfig.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;

/**
 * Annotate your config class with this annotation to access it in your core mod. BetterConfig will inject itself at the end of the static
 * initialization block of the config class to immediately load the config when it is accessed for the first time.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoadEarly {

	/**
	 * <p>
	 * Normally, {@link OnConfigChangedEvent} is fired after the config is loaded initially, but when loading a config early, Forge's event bus isn't
	 * available yet. Use this callback to process your config immediately after it was loaded.
	 * </p>
	 * 
	 * <pre><code>
	 * &#064;LoadEarly.Callback
	 * public static void afterEarlyLoad() {
	 *     // ...
	 * }
	 * </code></pre>
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface Callback {

	}

}
