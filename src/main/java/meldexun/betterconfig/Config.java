package meldexun.betterconfig;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
		Path temp = file.resolveSibling(".temp");
		try {
			try (ConfigWriter writer = new ConfigWriter(Files.newBufferedWriter(temp))) {
				writer.writeCommentLine("Configuration file");
				writer.newLine();
				for (Map.Entry<String, ConfigCategory> entry : this.categories.entrySet()) {
					String name = entry.getKey();
					ConfigCategory category = entry.getValue();
					Type type = getType.apply(name);
					BetterConfig settings = type != null ? AnnotationUtil.get(type, BetterConfig.class) : DEFAULT_SETTINGS;
					ConfigElementMetadata metadata = type != null ? ConfigElementMetadata.create(TypeUtil.getRawType(type)) : null;
					if (name.isEmpty()) {
						boolean writeComments = type != null && !TypeUtil.isMap(type);
						for (ConfigCategory.Entry entry1 : category.elements(settings, type, metadata, null)) {
							ConfigCategory.writeEntry(writer, settings, entry1.name(), entry1.configElement(), entry1.type(), entry1.metadata(), entry1.instance(), writeComments);
							writer.newLine();
							writer.newLine();
							writer.newLine();
						}
					} else {
						ConfigCategory.writeEntry(writer, settings, name, category, type, metadata, null, true);
						writer.newLine();
						writer.newLine();
						writer.newLine();
					}
				}
			}
			Files.move(temp, file, StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e) {
			try {
				Files.delete(temp);
			} catch (IOException e1) {
				e.addSuppressed(e1);
			}
			throw e;
		}
	}

	ConfigCategory getOrCreateCategory(String categoryName) {
		return this.categories.computeIfAbsent(categoryName, k -> new ConfigCategory());
	}

}
