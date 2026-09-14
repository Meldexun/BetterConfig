package meldexun.betterconfig.asm.patches.button;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;
import meldexun.betterconfig.asm.util.DeobfuscationUtil;

public class IGuiListEntryPatch {

	public static void register(IClassTransformerRegistry registry) {
		registry.add("net.minecraft.client.gui.GuiListExtended$IGuiListEntry", ClassWriter.COMPUTE_FRAMES, classNode -> {
			classNode.interfaces.add("meldexun/betterconfig/gui/IGuiListEntryExt");

			MethodNode mousePressedAll = new MethodNode(Opcodes.ACC_PUBLIC, "mousePressedAll", "(IIIIII)Z", null, null);
			mousePressedAll.instructions.insert(ASMUtil.listWithLabel(label -> ASMUtil.listOf(
					new VarInsnNode(Opcodes.ILOAD, 4),
					new JumpInsnNode(Opcodes.IFNE, label),
					new VarInsnNode(Opcodes.ALOAD, 0),
					new VarInsnNode(Opcodes.ILOAD, 1),
					new VarInsnNode(Opcodes.ILOAD, 2),
					new VarInsnNode(Opcodes.ILOAD, 3),
					new VarInsnNode(Opcodes.ILOAD, 4),
					new VarInsnNode(Opcodes.ILOAD, 5),
					new VarInsnNode(Opcodes.ILOAD, 6),
					DeobfuscationUtil.createObfMethodInsn(Opcodes.INVOKEINTERFACE, classNode.name, "func_148278_a", "(IIIIII)Z", true), // mousePressed
					new InsnNode(Opcodes.IRETURN),
					label,
					new InsnNode(Opcodes.ICONST_0),
					new InsnNode(Opcodes.IRETURN))));
			classNode.methods.add(mousePressedAll);
		});
	}

}
