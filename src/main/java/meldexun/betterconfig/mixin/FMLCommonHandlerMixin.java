package meldexun.betterconfig.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.BiMap;

import meldexun.betterconfig.TypeAdapters;
import meldexun.betterconfig.gui.configuration.ConfigurationGuiRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IFMLSidedHandler;
import net.minecraftforge.fml.common.ModContainer;

@Mixin(value = FMLCommonHandler.class, remap = false)
public abstract class FMLCommonHandlerMixin {

	@Shadow
	private BiMap<ModContainer, IModGuiFactory> guiFactories;

	@Inject(method = "beginLoading", at = @At("RETURN"))
	private void beginLoading(IFMLSidedHandler handler, CallbackInfoReturnable<List<String>> info) {
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
