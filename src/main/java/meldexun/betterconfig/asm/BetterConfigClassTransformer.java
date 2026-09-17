package meldexun.betterconfig.asm;

import java.lang.reflect.Field;

import org.objectweb.asm.ClassWriter;

import com.google.common.collect.BiMap;

import meldexun.asmutil2.HashMapClassNodeClassTransformer;
import meldexun.asmutil2.IClassTransformerRegistry;
import meldexun.asmutil2.NonLoadingClassWriter;
import meldexun.asmutil2.reader.ClassUtil;
import meldexun.betterconfig.asm.patches.FMLClientHandlerPatch;
import meldexun.betterconfig.asm.patches.FMLCommonHandlerPatch;
import meldexun.betterconfig.asm.patches.FMLModContainerPatch;
import meldexun.betterconfig.asm.patches.button.ForwardAllMouseEventsPatch;
import meldexun.betterconfig.asm.patches.button.GuiListExtendedPatch;
import meldexun.betterconfig.asm.patches.button.IGuiListEntryPatch;
import meldexun.betterconfig.asm.patches.hover.HoverCheckerPatch;
import meldexun.betterconfig.asm.patches.offset.ConfigButtonYOffsetPatch;
import meldexun.betterconfig.asm.patches.offset.GuiSlotPatch;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

public class BetterConfigClassTransformer extends HashMapClassNodeClassTransformer implements IClassTransformer {

	static final ClassUtil REMAPPING_CLASS_UTIL;
	static {
		try {
			Class<?> FMLDeobfuscatingRemapper = Class.forName("net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper", true, Launch.classLoader);
			Field _INSTANCE = FMLDeobfuscatingRemapper.getField("INSTANCE");
			Field _classNameBiMap = FMLDeobfuscatingRemapper.getDeclaredField("classNameBiMap");
			_classNameBiMap.setAccessible(true);
			@SuppressWarnings("unchecked")
			BiMap<String, String> deobfuscationMap = (BiMap<String, String>) _classNameBiMap.get(_INSTANCE.get(null));
			REMAPPING_CLASS_UTIL = ClassUtil.getInstance(new ClassUtil.Configuration(Launch.classLoader, deobfuscationMap.inverse(), deobfuscationMap));
		} catch (ReflectiveOperationException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	@Override
	protected void registerTransformers(IClassTransformerRegistry registry) {
		ForwardAllMouseEventsPatch.register(registry);
		GuiListExtendedPatch.register(registry);
		IGuiListEntryPatch.register(registry);
		HoverCheckerPatch.register(registry);
		ConfigButtonYOffsetPatch.register(registry);
		GuiSlotPatch.register(registry);
		FMLClientHandlerPatch.register(registry);
		FMLCommonHandlerPatch.register(registry);
		FMLModContainerPatch.register(registry);

	}

	@Override
	protected ClassWriter createClassWriter(int flags) {
		return new NonLoadingClassWriter(flags, REMAPPING_CLASS_UTIL);
	}

}
