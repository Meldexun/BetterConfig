package meldexun.betterconfig.mixin;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import meldexun.betterconfig.api.BetterConfig;
import meldexun.betterconfig.gui.ConfigCategoryGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory;

@Mixin(value = DefaultGuiFactory.class, remap = false)
public abstract class DefaultGuiFactoryMixin implements IModGuiFactory {

	@Shadow(remap = false)
	protected String modid;
	@Shadow(remap = false)
	protected String title;

	@Inject(method = "createConfigGui", remap = false, cancellable = true, at = @At("HEAD"))
	private void createConfigGui(GuiScreen parentScreen, CallbackInfoReturnable<GuiScreen> callback) {
		if (Arrays.stream(ConfigManager.getModConfigClasses(this.modid)).anyMatch(c -> c.isAnnotationPresent(BetterConfig.class))) {
			callback.setReturnValue(new ConfigCategoryGui(parentScreen, this.title, this.modid));
		}
	}

}
