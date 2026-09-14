package meldexun.betterconfig.asm.patches.hover;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;

public class HoverCheckerPatch {

	public static void register(IClassTransformerRegistry registry) {
		registry.add("net.minecraftforge.fml.client.config.HoverChecker", "checkHover", "(IIZ)Z", 0, method -> {
			method.instructions.insertBefore(ASMUtil.first(method).fieldInsn("bottom").opcode(Opcodes.PUTFIELD).find(), ASMUtil.listOf(
					new InsnNode(Opcodes.ICONST_1),
					new InsnNode(Opcodes.ISUB)));
			method.instructions.insertBefore(ASMUtil.first(method).fieldInsn("right").opcode(Opcodes.PUTFIELD).find(), ASMUtil.listOf(
					new InsnNode(Opcodes.ICONST_1),
					new InsnNode(Opcodes.ISUB)));
		});
	}

}
