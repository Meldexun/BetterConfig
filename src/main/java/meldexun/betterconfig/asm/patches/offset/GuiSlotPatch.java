package meldexun.betterconfig.asm.patches.offset;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;

public class GuiSlotPatch {

	public static void register(IClassTransformerRegistry registry) {
		registry.add("net.minecraft.client.gui.GuiSlot", ClassWriter.COMPUTE_MAXS, classNode -> {
			classNode.interfaces.add("meldexun/betterconfig/gui/GuiSlotExt");

			MethodNode drawScreen = ASMUtil.findObf(classNode, "drawScreen", "func_148128_a");
			ASMUtil.replace(drawScreen, ASMUtil.first(drawScreen).opcode(Opcodes.ICONST_2).ordinal(2).find(), ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new MethodInsnNode(Opcodes.INVOKEINTERFACE, "meldexun/betterconfig/gui/GuiSlotExt", "offsetLeft", "()I", true)));

			MethodNode offsetLeft = new MethodNode(Opcodes.ACC_PUBLIC, "offsetLeft", "()I", null, null);
			offsetLeft.instructions.insert(ASMUtil.listOf(
					new InsnNode(Opcodes.ICONST_2),
					new InsnNode(Opcodes.IRETURN)));
			classNode.methods.add(offsetLeft);
		});
	}

}
