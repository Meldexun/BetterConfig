package meldexun.betterconfig.asm;

import java.util.Map;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;

import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions("meldexun.betterconfig.asm")
public class BetterConfigPlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return null;
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		if (Boolean.FALSE.equals(data.get("runtimeDeobfuscationEnabled"))) {
			MixinBootstrap.init();
			MixinExtrasBootstrap.init();
			MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
			CoreModManager.getIgnoredMods().add("mixin-0.8.7.jar");
			CoreModManager.getIgnoredMods().add("asm-util-6.2.jar");
			CoreModManager.getIgnoredMods().add("asm-analysis-6.2.jar");
			CoreModManager.getIgnoredMods().add("asm-tree-6.2.jar");
			CoreModManager.getIgnoredMods().add("asm-6.2.jar");
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
