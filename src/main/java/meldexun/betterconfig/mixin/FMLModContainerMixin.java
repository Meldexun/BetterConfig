package meldexun.betterconfig.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.betterconfig.ConfigManager;
import meldexun.betterconfig.api.BetterConfig;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;

@Mixin(value = FMLModContainer.class, remap = false)
public abstract class FMLModContainerMixin implements ModContainer {

	@Inject(method = "constructMod", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/config/ConfigManager;sync(Ljava/lang/String;Lnet/minecraftforge/common/config/Config$Type;)V", shift = Shift.AFTER))
	public void constructMod(FMLConstructionEvent event, CallbackInfo info) {
		for (ASMData target : event.getASMHarvestedData().getAnnotationsFor(this).get(BetterConfig.class.getName())) {
			try {
				ConfigManager.registerAndLoad(Class.forName(target.getClassName().replace('/', '.')));
			} catch (ClassNotFoundException e) {
				throw new LoaderException(e);
			}
		}
	}

	@Shadow
	public abstract String getModId();

}
