package meldexun.betterconfig.asm.patches.offset;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;

public class ConfigButtonYOffsetPatch {

	public static void register(IClassTransformerRegistry registry) {
		registry.add("net.minecraftforge.fml.client.config.GuiConfigEntries$ListEntryBase", ClassWriter.COMPUTE_MAXS, classNode -> {
			classNode.interfaces.add("meldexun/betterconfig/gui/ConfigGuiEntry");

			MethodNode drawEntry = ASMUtil.findObf(classNode, "drawEntry", "func_192634_a");
			drawEntry.instructions.insertBefore(ASMUtil.first(drawEntry).fieldInsnObf("y", "field_146129_i").ordinal(0).find(), ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new MethodInsnNode(Opcodes.INVOKEINTERFACE, "meldexun/betterconfig/gui/ConfigGuiEntry", "buttonOffsetY", "()I", true),
					new InsnNode(Opcodes.IADD)));
			drawEntry.instructions.insertBefore(ASMUtil.first(drawEntry).fieldInsnObf("y", "field_146129_i").ordinal(1).find(), ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new MethodInsnNode(Opcodes.INVOKEINTERFACE, "meldexun/betterconfig/gui/ConfigGuiEntry", "buttonOffsetY", "()I", true),
					new InsnNode(Opcodes.IADD)));

			MethodNode buttonOffsetY = new MethodNode(Opcodes.ACC_PUBLIC, "buttonOffsetY", "()I", null, null);
			buttonOffsetY.instructions.insert(ASMUtil.listOf(
					new InsnNode(Opcodes.ICONST_0),
					new InsnNode(Opcodes.IRETURN)));
			classNode.methods.add(buttonOffsetY);
		});
		registry.add("net.minecraftforge.fml.client.config.GuiEditArrayEntries$BaseEntry", ClassWriter.COMPUTE_MAXS, classNode -> {
			classNode.interfaces.add("meldexun/betterconfig/gui/ConfigGuiEntry");

			MethodNode drawEntry = ASMUtil.findObf(classNode, "drawEntry", "func_192634_a");
			drawEntry.instructions.insertBefore(ASMUtil.first(drawEntry).fieldInsnObf("y", "field_146129_i").ordinal(0).find(), ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new MethodInsnNode(Opcodes.INVOKEINTERFACE, "meldexun/betterconfig/gui/ConfigGuiEntry", "buttonOffsetY", "()I", true),
					new InsnNode(Opcodes.IADD)));
			drawEntry.instructions.insertBefore(ASMUtil.first(drawEntry).fieldInsnObf("y", "field_146129_i").ordinal(1).find(), ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new MethodInsnNode(Opcodes.INVOKEINTERFACE, "meldexun/betterconfig/gui/ConfigGuiEntry", "buttonOffsetY", "()I", true),
					new InsnNode(Opcodes.IADD)));

			MethodNode buttonOffsetY = new MethodNode(Opcodes.ACC_PUBLIC, "buttonOffsetY", "()I", null, null);
			buttonOffsetY.instructions.insert(ASMUtil.listOf(
					new InsnNode(Opcodes.ICONST_0),
					new InsnNode(Opcodes.IRETURN)));
			classNode.methods.add(buttonOffsetY);
		});
	}

}
