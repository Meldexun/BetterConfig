package meldexun.betterconfig.asm;

import meldexun.asmutil2.HashMapClassNodeClassTransformer;
import meldexun.asmutil2.IClassTransformerRegistry;
import meldexun.betterconfig.asm.patches.FMLClientHandlerPatch;
import meldexun.betterconfig.asm.patches.FMLCommonHandlerPatch;
import meldexun.betterconfig.asm.patches.FMLModContainerPatch;
import meldexun.betterconfig.asm.patches.button.ForwardAllMouseEventsPatch;
import meldexun.betterconfig.asm.patches.button.GuiListExtendedPatch;
import meldexun.betterconfig.asm.patches.button.IGuiListEntryPatch;
import meldexun.betterconfig.asm.patches.hover.HoverCheckerPatch;
import meldexun.betterconfig.asm.patches.offset.ConfigButtonYOffsetPatch;
import meldexun.betterconfig.asm.patches.offset.GuiSlotPatch;
import net.minecraft.launchwrapper.IClassTransformer;

public class BetterConfigClassTransformer extends HashMapClassNodeClassTransformer implements IClassTransformer {

	@Override
	protected void registerTransformers(IClassTransformerRegistry registry) {
		ForwardAllMouseEventsPatch.register(registry);
		GuiListExtendedPatch.register(registry);
		IGuiListEntryPatch.register(registry);
		HoverCheckerPatch.register(registry);
		ConfigButtonYOffsetPatch.register(registry);
		GuiSlotPatch.register(registry);
		FMLClientHandlerPatch.register(registry);
		FMLCommonHandlerPatch.register(registry);
		FMLModContainerPatch.register(registry);

	}

}
