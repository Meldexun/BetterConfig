package meldexun.betterconfig.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.google.common.collect.BiMap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import meldexun.betterconfig.ConfigManager;
import meldexun.betterconfig.gui.ConfigCategoryGui;
import meldexun.betterconfig.gui.configuration.ConfigurationGuiFactory;
import meldexun.betterconfig.gui.configuration.ConfigurationGuiRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.common.ModContainer;

@Mixin(value = FMLClientHandler.class, remap = false)
public abstract class FMLClientHandlerMixin implements IModGuiFactory {

	@Shadow
	private BiMap<ModContainer, IModGuiFactory> guiFactories;

	@ModifyExpressionValue(method = "finishMinecraftLoading", at = @At(value = "INVOKE", target = "Lcom/google/common/base/Strings;isNullOrEmpty(Ljava/lang/String;)Z"))
	private boolean finishMinecraftLoading(boolean isNullOrEmpty, @Local ModContainer modContainer) {
		if(!isNullOrEmpty) return isNullOrEmpty;

		if (ConfigManager.has(modContainer.getModId())) {
			this.guiFactories.put(modContainer, new IModGuiFactory() {
				@Override
				public void initialize(Minecraft minecraftInstance) {

				}

				@Override
				public boolean hasConfigGui() {
					return true;
				}

				@Override
				public GuiScreen createConfigGui(GuiScreen parentScreen) {
					return new ConfigCategoryGui(parentScreen, modContainer.getName(), modContainer.getModId());
				}

				@Override
				public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
					return null;
				}
			});
		} else if (ConfigurationGuiRegistry.hasGuiFor(modContainer.getModId())) {
			if (this.guiFactories.containsKey(modContainer)) {
				ConfigurationGuiRegistry.unregister(modContainer.getModId());
			} else {
				this.guiFactories.put(modContainer, new ConfigurationGuiFactory(modContainer.getModId(), modContainer.getName()));
			}
		}
		return isNullOrEmpty;
	}

}
