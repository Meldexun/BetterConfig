package meldexun.betterconfig;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;

import meldexun.betterconfig.api.BetterConfig;
import meldexun.betterconfig.gui.EntryInfo;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.LoaderException;

public class ConfigurationManager {

	private static final Path CONFIG_DIRECTORY = (Launch.minecraftHome != null ? Launch.minecraftHome : new File(".")).toPath().resolve("config");
	private static final Map<String, SetMultimap<Path, Class<?>>> MODID_2_FILE_2_CONFIG_CLASSES = new HashMap<>();
	private static final Map<Path, Config> CONFIGS = new HashMap<>();
	private static final SetMultimap<Path, String> LOADED_CATEGORIES = HashMultimap.create();

	public static synchronized void register(Class<?> configClass) {
		BetterConfig configAnnotation = AnnotationUtil.getOrThrow(configClass, BetterConfig.class);
		String modid = configAnnotation.modid();
		if (StringUtils.isBlank(modid)) {
			throw new LoaderException("BetterConfig annotation modid of class " + configClass.getName() + " may not be blank!");
		}
		String configName = !configAnnotation.name().isEmpty() ? configAnnotation.name() : modid;
		Path file = CONFIG_DIRECTORY.resolve(configName + ".cfg");
		MODID_2_FILE_2_CONFIG_CLASSES.computeIfAbsent(modid, k -> HashMultimap.create()).put(file, configClass);
	}

	public static synchronized void sync(String modid) {
		MODID_2_FILE_2_CONFIG_CLASSES.getOrDefault(modid, ImmutableSetMultimap.of())
				.asMap()
				.forEach((file, configClasses) -> {
					try {
						Config config = CONFIGS.computeIfAbsent(file, k -> {
							try {
								Config v = new Config();
								v.load(k);
								return v;
							} catch (IOException e) {
								throw new UncheckedIOException(e);
							}
						});

						for (Class<?> configClass : configClasses) {
							BetterConfig settings = AnnotationUtil.getOrThrow(configClass, BetterConfig.class);
							String categoryName = settings.category();
							ConfigCategory category = config.getOrCreateCategory(categoryName);
							if (LOADED_CATEGORIES.put(file, categoryName)) {
								category.loadInfo(settings, configClass, EntryInfo.create(configClass), null);
								category.loadFromConfig(settings, configClass, null);
							}
							category.saveToConfig(settings, configClass, null);
						}

						config.save(file, configClasses.stream()
								.map(c -> AnnotationUtil.getOrThrow(c, BetterConfig.class))
								.collect(Collectors.toMap(BetterConfig::category, Function.identity()))::get);
					} catch (Exception e) {
						throw new LoaderException(e);
					}
				});
	}

	public static synchronized boolean has(String modId) {
		return MODID_2_FILE_2_CONFIG_CLASSES.containsKey(modId);
	}

	public static synchronized Class<?>[] get(String modid) {
		return MODID_2_FILE_2_CONFIG_CLASSES.get(modid).values().toArray(new Class[0]);
	}

}
