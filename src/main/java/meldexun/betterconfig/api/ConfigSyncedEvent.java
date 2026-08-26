package meldexun.betterconfig.api;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/**
 * Only fired for config classes annotated with {@link Sync}.<br>
 * <br>
 * Fired on logical client side after copying config options from a sync packet to the runtime config class.<br>
 * <br>
 * Sync packets are sent when {@link PlayerLoggedInEvent} is fired and when changing a config using the ingame GUI while an integrated server is running.
 */
public class ConfigSyncedEvent extends Event {

	private final Class<?> slaveConfigClass;

	public ConfigSyncedEvent(Class<?> slaveConfigClass) {
		this.slaveConfigClass = slaveConfigClass;
	}

	public Class<?> getSlaveConfigClass() {
		return slaveConfigClass;
	}

}
