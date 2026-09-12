package meldexun.betterconfig.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class BetterConfigGuiFactory implements IModGuiFactory {

	private final String modId;
	private final String modName;

	public BetterConfigGuiFactory(String modId, String modName) {
		this.modId = modId;
		this.modName = modName;
	}

	@Override
	public void initialize(Minecraft minecraftInstance) {

	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new ConfigCategoryGui(parentScreen, this.modName, this.modId);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

}