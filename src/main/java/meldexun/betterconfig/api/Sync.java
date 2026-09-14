package meldexun.betterconfig.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate your config class with this annotation to automatically synchronize this config between server and client.<br>
 * <br>
 * Internally BetterConfig creates a copy of the config class. This copy is called the local config and represents the settings of the client. The
 * local config is always what a player can edit when using the ingame GUI. The original config class, the runtime config, represents the settings of
 * the server. Mod devs using BetterConfig do not have access to the internal config.<br>
 * <br>
 * When joining a server (launching a single player world also counts) or when the config on the server changes, BetterConfig will send the config to
 * the client and replace the runtime config with the settings from the server. After that the {@link ConfigSyncedEvent} will be fired.<br>
 * When a server starts BetterConfig will copy the settings from the local config to the runtime config. After that the {@link ConfigCopiedEvent} will
 * be fired.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Sync {

}
