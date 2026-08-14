package meldexun.betterconfig.api;

import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Only fired for config classes annotated with {@link Sync}.<br>
 * <br>
 * Fired on logical server side after copying config options from the local config class to the runtime config class.<br>
 * <br>
 * This happens when {@link FMLServerAboutToStartEvent} is fired and when changing a config using the ingame GUI while an integrated server is running.
 */
public class ConfigCopiedEvent extends Event {

	private final Class<?> configClass;

	public ConfigCopiedEvent(Class<?> configClass) {
		this.configClass = configClass;
	}

	public Class<?> getConfigClass() {
		return configClass;
	}

}
