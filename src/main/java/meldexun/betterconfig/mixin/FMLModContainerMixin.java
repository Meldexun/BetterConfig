package meldexun.betterconfig.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import meldexun.betterconfig.ConfigurationManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.FMLModContainer;

@Mixin(FMLModContainer.class)
public class FMLModContainerMixin {

	@Redirect(method = "constructMod", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/config/ConfigManager;sync(Ljava/lang/String;Lnet/minecraftforge/common/config/Config$Type;)V"))
	public void sync(String modid, Config.Type configType) {
		ConfigurationManager.sync(modid, configType);
	}

}
