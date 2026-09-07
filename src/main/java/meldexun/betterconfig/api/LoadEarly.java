package meldexun.betterconfig.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoadEarly {

	/**
	 * <p>
	 * Normally, {@link OnConfigChangedEvent} is fired after the config is loaded initially, but when loading a config early, Forge's event bus isn't available yet. Use this callback to process your config immediately after it was loaded.
	 * </p>
	 * 
	 * <pre>{@code
	 * @LoadEarly.Callback
	 * public static void afterEarlyLoad() {
	 *     // ...
	 * }
	 * }</pre>
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface Callback {

	}

}
