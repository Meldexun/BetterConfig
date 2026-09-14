package meldexun.betterconfig.asm.patches.button;

import org.objectweb.asm.tree.MethodInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;

public class GuiListExtendedPatch {

	public static void register(IClassTransformerRegistry registry) {
		registry.addObf("net.minecraft.client.gui.GuiListExtended", "mouseClicked", "func_148179_a", 0, method -> {
			MethodInsnNode mousePressed = ASMUtil.first(method).methodInsnObf("mousePressed", "func_148278_a").find();
			mousePressed.owner = "meldexun/betterconfig/gui/IGuiListEntryExt";
			mousePressed.name = "mousePressedAll";
			mousePressed.itf = true;
		});
	}

}
