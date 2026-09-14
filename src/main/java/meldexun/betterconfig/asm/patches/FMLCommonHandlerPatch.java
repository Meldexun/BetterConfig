package meldexun.betterconfig.asm.patches;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;
import meldexun.betterconfig.TypeAdapters;
import meldexun.betterconfig.gui.configuration.ConfigurationGuiRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class FMLCommonHandlerPatch {

	public static void register(IClassTransformerRegistry registry) {
		registry.add("net.minecraftforge.fml.common.FMLCommonHandler", "beginLoading", 0, method -> {
			method.instructions.insertBefore(ASMUtil.last(method).opcode(Opcodes.ARETURN).find(), ASMUtil.listOf(
					new MethodInsnNode(Opcodes.INVOKESTATIC, "meldexun/betterconfig/asm/patches/FMLCommonHandlerPatch$Hook", "initBetterConfig", "()V", false)));
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

	}

}
