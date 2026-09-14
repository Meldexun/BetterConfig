package meldexun.betterconfig.asm.patches;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.BiMap;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;
import meldexun.betterconfig.ConfigManager;
import meldexun.betterconfig.gui.BetterConfigGuiFactory;
import meldexun.betterconfig.gui.configuration.ConfigurationGuiFactory;
import meldexun.betterconfig.gui.configuration.ConfigurationGuiRegistry;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.common.ModContainer;

public class FMLClientHandlerPatch {

	public static void register(IClassTransformerRegistry registry) {
		registry.add("net.minecraftforge.fml.client.FMLClientHandler", "finishMinecraftLoading", 0, method -> {
			method.instructions.insert(ASMUtil.first(method).methodInsn("isNullOrEmpty").findThenNext().type(JumpInsnNode.class).find(), ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new FieldInsnNode(Opcodes.GETFIELD, "net/minecraftforge/fml/client/FMLClientHandler", "guiFactories", "Lcom/google/common/collect/BiMap;"),
					new VarInsnNode(Opcodes.ALOAD, ASMUtil.findLocalVariable(method, "mc").index),
					new MethodInsnNode(Opcodes.INVOKESTATIC, "meldexun/betterconfig/asm/patches/FMLClientHandlerPatch$Hook", "registerConfigGUIFactories", "(Lcom/google/common/collect/BiMap;Lnet/minecraftforge/fml/common/ModContainer;)V", false)));
		});
	}

	public static class Hook {

		public static void registerConfigGUIFactories(BiMap<ModContainer, IModGuiFactory> guiFactories, ModContainer modContainer) {
			if (ConfigManager.has(modContainer.getModId())) {
				guiFactories.put(modContainer, new BetterConfigGuiFactory(modContainer.getModId(), modContainer.getName()));
			} else if (ConfigurationGuiRegistry.hasGuiFor(modContainer.getModId())) {
				if (guiFactories.containsKey(modContainer)) {
					ConfigurationGuiRegistry.unregister(modContainer.getModId());
				} else {
					guiFactories.put(modContainer, new ConfigurationGuiFactory(modContainer.getModId(), modContainer.getName()));
				}
			}
		}

	}

}
