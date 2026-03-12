package meldexun.betterconfig.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.HashMapClassNodeClassTransformer;
import meldexun.asmutil2.IClassTransformerRegistry;
import meldexun.betterconfig.ConfigManager;
import meldexun.betterconfig.TypeAdapters;
import meldexun.betterconfig.api.BetterConfig;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;

public class BetterConfigClassTransformer extends HashMapClassNodeClassTransformer implements IClassTransformer {

	@Override
	protected void registerTransformers(IClassTransformerRegistry registry) {
		registry.add("net.minecraftforge.fml.common.Loader", "loadMods", 0, method -> {
			method.instructions.insert(ASMUtil.first(method).methodInsn("loadData").find(), ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new FieldInsnNode(Opcodes.GETFIELD, "net/minecraftforge/fml/common/Loader", "discoverer", "Lnet/minecraftforge/fml/common/discovery/ModDiscoverer;"),
					new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/discovery/ModDiscoverer", "getASMTable", "()Lnet/minecraftforge/fml/common/discovery/ASMDataTable;", false),
					new MethodInsnNode(Opcodes.INVOKESTATIC, BetterConfigClassTransformer.class.getName().replace('.', '/') + "$Hook", "loadBetterConfigClasses", "(Lnet/minecraftforge/fml/common/discovery/ASMDataTable;)V", false)));
		});
	}

	public static class Hook {

		public static void loadBetterConfigClasses(ASMDataTable asmDataTable) {
			TypeAdapters.register(ResourceLocation::toString, ResourceLocation::new, new ResourceLocation("unkown"), ResourceLocation.class);

			for (ASMData target : asmDataTable.getAll(BetterConfig.class.getName())) {
				try {
					ConfigManager.register(Class.forName(target.getClassName().replace('/', '.')));
				} catch (ClassNotFoundException e) {
					throw new LoaderException(e);
				}
			}
		}

	}

}
