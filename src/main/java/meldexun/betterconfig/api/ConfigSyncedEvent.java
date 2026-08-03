package meldexun.betterconfig.api;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ConfigSyncedEvent extends Event {

	private final Class<?> slaveConfigClass;

	public ConfigSyncedEvent(Class<?> slaveConfigClass) {
		this.slaveConfigClass = slaveConfigClass;
	}

	public Class<?> getSlaveConfigClass() {
		return slaveConfigClass;
	}

}
