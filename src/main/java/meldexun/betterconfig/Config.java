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
import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;

import meldexun.betterconfig.api.BetterConfig;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

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
		public String version() {
			return "";
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
	private static final Pattern CONFIG_VERSION = Pattern.compile("~CONFIG_VERSION\\(([^)]+)\\)\\s*:\\s*(.+)");
	private final Map<String, ConfigCategory> categories = new HashMap<>();
	private final Map<String, ArtifactVersion> versions = new HashMap<>();

	public ArtifactVersion getVersion(String className) {
		return this.versions.get(className);
	}

	public void setVersion(String className, ArtifactVersion version) {
		this.versions.put(className, version);
	}

	void load(Path file) throws IOException {
		this.categories.clear();
		this.versions.clear();
		if (Files.exists(file)) {
			try (ConfigReader reader = new ConfigReader(Files.newBufferedReader(file))) {
				this.readVersions(reader);

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

	private void readVersions(ConfigReader reader) throws IOException {
		while (reader.hasRawNext()) {
			String line = reader.peekRawLine().trim();
			if (line.startsWith("#")) {
				reader.readRawLine();
			} else if (line.isEmpty()) {
				reader.readRawLine();
				if (!this.versions.isEmpty()) {
					break; // Empty line after versions marks end of header section
				}
			} else if (line.startsWith("~CONFIG_VERSION")) {
				Matcher matcher = reader.readRawMatching(CONFIG_VERSION);
				if (matcher != null) {
					this.versions.put(matcher.group(1), new DefaultArtifactVersion(matcher.group(2)));
				}
				reader.readRawLine();
			} else {
				break; // Non-header line, don't consume
			}
		}
	}

	void save(Path file, Function<String, Type> getType) throws IOException {
		Path temp = file.resolveSibling(".temp");
		try {
			try (ConfigWriter writer = new ConfigWriter(Files.newBufferedWriter(temp))) {
				writer.writeCommentLine("Configuration file");
				writer.newLine();

				if (!this.versions.isEmpty()) {
					for (Map.Entry<String, ArtifactVersion> entry : this.versions.entrySet()) {
						writer.writeLine("~CONFIG_VERSION(" + entry.getKey() + "): " + entry.getValue().getVersionString());
					}
					writer.newLine();
				}

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
