package meldexun.betterconfig.asm;

import java.util.Map;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({ "meldexun.betterconfig.asm", "meldexun.asmutil2" })
@IFMLLoadingPlugin.SortingIndex(1001)
public class BetterConfigPlugin implements IFMLLoadingPlugin {

	public BetterConfigPlugin() {
		Launch.classLoader.registerTransformer(LoadEarlyClassTransformer.class.getName());
		Launch.classLoader.registerTransformer(ConfigurationGuiClassTransformer.class.getName());
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { BetterConfigClassTransformer.class.getName() };
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
