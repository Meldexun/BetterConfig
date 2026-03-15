package meldexun.betterconfig;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;

import org.apache.commons.lang3.ObjectUtils;

import meldexun.betterconfig.api.BetterConfig;

class Config {

	private static final BetterConfig DEFAULT_SETTINGS = new BetterConfig() {
		@Override
		public Class<? extends Annotation> annotationType() {
			return BetterConfig.class;
		}

		@Override
		public String modid() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String name() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String category() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean lowerCaseCategories() {
			return true;
		}

		@Override
		public boolean bigCategoryComments() {
			return true;
		}

		@Override
		public boolean addRangesToComments() {
			return true;
		}

		@Override
		public boolean addDefaultsToComments() {
			return true;
		}

		@Override
		public boolean removeDeprecatedEntries() {
			return false;
		}

		@Override
		public ConfigComparator[] elementOrder() {
			return null;
		}
	};
	private final Map<String, ConfigCategory> categories = new HashMap<>();

	void load(Path file) throws IOException {
		this.categories.clear();
		if (Files.exists(file)) {
			try (ConfigReader reader = new ConfigReader(Files.newBufferedReader(file))) {
				while (reader.hasNext()) {
					Matcher matcher;
					if ((matcher = reader.readMatching(ConfigCategory.CATEGORY)) != null) {
						String name = ObjectUtils.defaultIfNull(matcher.group(1), matcher.group(2));
						this.getOrCreateCategory(name).read(reader);
					} else {
						throw new IllegalArgumentException();
					}
				}
			}
		}
	}

	void save(Path file, Function<String, Type> getType) throws IOException {
		try (ConfigWriter writer = new ConfigWriter(Files.newBufferedWriter(file))) {
			writer.writeCommentLine("Configuration file");
			writer.newLine();
			for (Map.Entry<String, ConfigCategory> entry : this.categories.entrySet()) {
				String name = entry.getKey();
				ConfigCategory category = entry.getValue();
				Type type = ObjectUtils.defaultIfNull(getType.apply(name), Map.class);
				BetterConfig settings = ObjectUtils.defaultIfNull(AnnotationUtil.get(type, BetterConfig.class), DEFAULT_SETTINGS);
				if (name.isEmpty()) {
					for (ConfigCategory.Entry entry1 : category.elements(settings, type, ConfigElementMetadata.create(TypeUtil.getRawType(type)), null)) {
						ConfigCategory.writeEntry(writer, settings, entry1.name(), entry1.configElement(), entry1.type(), entry1.metadata(), entry1.instance(), !TypeUtil.isMap(type));
						writer.newLine();
						writer.newLine();
						writer.newLine();
					}
				} else {
					ConfigCategory.writeEntry(writer, settings, name, category, type, ConfigElementMetadata.create(TypeUtil.getRawType(type)), null, true);
					writer.newLine();
					writer.newLine();
					writer.newLine();
				}
			}
		}
	}

	ConfigCategory getOrCreateCategory(String categoryName) {
		return this.categories.computeIfAbsent(categoryName, k -> new ConfigCategory());
	}

}
