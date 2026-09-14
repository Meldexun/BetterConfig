package meldexun.betterconfig.asm.patches.button;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;

public class ForwardAllMouseEventsPatch {

	public static void register(IClassTransformerRegistry registry) {
		registry.addObf("net.minecraftforge.fml.client.config.GuiConfig", "mouseClicked", "func_73864_a", 0, method -> {
			VarInsnNode iload_mouseEvent = ASMUtil.first(method).varInsn(3).opcode(Opcodes.ILOAD).find();
			if (iload_mouseEvent.getNext().getOpcode() != Opcodes.IFNE) throw new IllegalStateException();
			ASMUtil.remove(method, iload_mouseEvent, iload_mouseEvent.getNext());
		});
		registry.addObf("net.minecraftforge.fml.client.config.GuiEditArray", "mouseClicked", "func_73864_a", 0, method -> {
			VarInsnNode iload_mouseEvent = ASMUtil.first(method).varInsn(3).opcode(Opcodes.ILOAD).find();
			if (iload_mouseEvent.getNext().getOpcode() != Opcodes.IFNE) throw new IllegalStateException();
			ASMUtil.remove(method, iload_mouseEvent, iload_mouseEvent.getNext());
		});
	}

}
