package meldexun.betterconfig.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.betterconfig.ConfigurationManager;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;

@Mixin(value = FMLModContainer.class, remap = false)
public abstract class FMLModContainerMixin {

	@Inject(method = "constructMod", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/config/ConfigManager;sync(Ljava/lang/String;Lnet/minecraftforge/common/config/Config$Type;)V", shift = Shift.AFTER))
	public void constructMod(FMLConstructionEvent event, CallbackInfo info) {
		ConfigurationManager.sync(this.getModId());
	}

	@Shadow
	public abstract String getModId();

}
