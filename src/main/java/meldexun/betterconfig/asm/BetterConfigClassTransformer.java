package meldexun.betterconfig.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.HashMapClassNodeClassTransformer;
import meldexun.asmutil2.IClassTransformerRegistry;
import meldexun.betterconfig.ConfigManager;
import meldexun.betterconfig.TypeAdapters;
import meldexun.betterconfig.api.BetterConfig;
import meldexun.betterconfig.gui.configuration.ConfigurationGuiRegistry;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;

public class BetterConfigClassTransformer extends HashMapClassNodeClassTransformer implements IClassTransformer {

	@Override
	protected void registerTransformers(IClassTransformerRegistry registry) {
		registry.add("net.minecraftforge.fml.common.Loader", "loadMods", 0, method -> {
			method.instructions.insert(ASMUtil.first(method).methodInsn("loadData").find(), ASMUtil.listOf(
					new MethodInsnNode(Opcodes.INVOKESTATIC, BetterConfigClassTransformer.class.getName().replace('.', '/') + "$Hook", "initBetterConfig", "()V", false)));
		});
		registry.add("net.minecraftforge.fml.common.FMLModContainer", "constructMod", 0, method -> {
			method.instructions.insert(ASMUtil.first(method).methodInsn("sync").find(), ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new VarInsnNode(Opcodes.ALOAD, 1),
					new MethodInsnNode(Opcodes.INVOKESTATIC, BetterConfigClassTransformer.class.getName().replace('.', '/') + "$Hook", "loadBetterConfigClasses", "(Lnet/minecraftforge/fml/common/FMLModContainer;Lnet/minecraftforge/fml/common/event/FMLConstructionEvent;)V", false)));
		});
	}

	public static class Hook {

		public static void initBetterConfig() {
			TypeAdapters.register(ResourceLocation::toString, ResourceLocation::new, new ResourceLocation("unkown"), ResourceLocation.class);

			TypeAdapters.register(v -> v.getX() + "," + v.getY() + "," + v.getZ(), s -> {
				String[] a = s.split(",");
				if (a.length != 3) throw new IllegalArgumentException();
				return new Vec3i(
						Integer.parseInt(a[0].trim()),
						Integer.parseInt(a[1].trim()),
						Integer.parseInt(a[2].trim()));
			}, Vec3i.NULL_VECTOR, Vec3i.class);
			TypeAdapters.register(v -> v.x + "," + v.y + "," + v.z, s -> {
				String[] a = s.split(",");
				if (a.length != 3) throw new IllegalArgumentException();
				return new Vec3d(
						Double.parseDouble(a[0].trim()),
						Double.parseDouble(a[1].trim()),
						Double.parseDouble(a[2].trim()));
			}, Vec3d.ZERO, Vec3d.class);
			TypeAdapters.register(v -> v.getX() + "," + v.getY() + "," + v.getZ(), s -> {
				String[] a = s.split(",");
				if (a.length != 3) throw new IllegalArgumentException();
				return new BlockPos(
						Integer.parseInt(a[0].trim()),
						Integer.parseInt(a[1].trim()),
						Integer.parseInt(a[2].trim()));
			}, BlockPos.ORIGIN, BlockPos.class);

			ConfigurationGuiRegistry.enableRegistration();
		}

		public static void loadBetterConfigClasses(ModContainer modContainer, FMLConstructionEvent event) {
			for (ASMData target : event.getASMHarvestedData().getAnnotationsFor(modContainer).get(BetterConfig.class.getName())) {
				try {
					ConfigManager.register(Class.forName(target.getClassName().replace('/', '.')));
				} catch (ClassNotFoundException e) {
					throw new LoaderException(e);
				}
			}
		}

	}

}
