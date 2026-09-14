package meldexun.betterconfig.api;

import meldexun.betterconfig.ConfigManager;
import net.minecraftforge.common.config.Config.Type;

public class BetterConfigManager {

	/**
	 * Replacement for {@link net.minecraftforge.common.config.ConfigManager#sync(String, Type) ConfigManager.sync(String, Type)}.<br>
	 * <br>
	 * This will load the configs matching the provided mod id if they were not loaded yet for some reason. Then the settings of these config classes will
	 * be stored in the in-memory representation of the actual config files. Finally the updated configs will be saved to disk.
	 */
	public static void sync(String modid) {
		ConfigManager.sync(modid);
	}

}
