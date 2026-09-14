package meldexun.betterconfig.asm.patches;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;
import meldexun.betterconfig.ConfigManager;
import meldexun.betterconfig.api.BetterConfig;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;

public class FMLModContainerPatch {

	public static void register(IClassTransformerRegistry registry) {
		registry.add("net.minecraftforge.fml.common.FMLModContainer", "constructMod", 0, method -> {
			method.instructions.insert(ASMUtil.first(method).methodInsn("sync").find(), ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new VarInsnNode(Opcodes.ALOAD, 1),
					new MethodInsnNode(Opcodes.INVOKESTATIC, "meldexun/betterconfig/asm/patches/FMLModContainerPatch$Hook", "registerConfigs", "(Lnet/minecraftforge/fml/common/FMLModContainer;Lnet/minecraftforge/fml/common/event/FMLConstructionEvent;)V", false)));
		});
	}

	public static class Hook {

		public static void registerConfigs(FMLModContainer modContainer, FMLConstructionEvent event) {
			for (ASMData target : event.getASMHarvestedData().getAnnotationsFor(modContainer).get(BetterConfig.class.getName())) {
				try {
					ConfigManager.registerAndLoad(Class.forName(target.getClassName().replace('/', '.')));
				} catch (ClassNotFoundException e) {
					throw new LoaderException(e);
				}
			}
		}

	}

}
