package meldexun.betterconfig.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import meldexun.betterconfig.ConfigUtil;
import meldexun.betterconfig.api.Order;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;

public class ConfigCategoryGui extends GuiConfig implements TitledGui, ConfigGui {

	private final Supplier<String> titleSupplier;

	public ConfigCategoryGui(GuiScreen parentScreen, String title, String modID) {
		super(parentScreen, Collections.emptyList(), modID, null, false, false, null, null);
		this.titleSupplier = () -> title;
		this.entryList = new Entries(ConfigManager.getModConfigClasses(modID));
	}

	public ConfigCategoryGui(GuiScreen parentScreen, Supplier<String> titleSupplier, Type type, @Nullable Object instance) {
		super(parentScreen, Collections.emptyList(), "UNKNOWN", null, false, false, null, null);
		this.titleSupplier = titleSupplier;
		this.entryList = new Entries(type, instance);
	}

	@Override
	public void initGui() {
		if (this.parentScreen instanceof TitledGui) {
			this.title = ((TitledGui) this.parentScreen).title();
			this.titleLine2 = ((TitledGui) this.parentScreen).subscreen(this.titleSupplier.get());
		} else {
			this.title = this.titleSupplier.get();
			this.titleLine2 = null;
		}
		this.needsRefresh = false;
		super.initGui();
	}

	@Override
	public String title() {
		return this.title;
	}

	@Override
	public String subtitle() {
		return this.titleLine2;
	}

	@Override
	public void recalculateState() {

	}

	public class Entries extends GuiConfigEntries implements GuiSlotExt {

		public Entries(Class<?>... configTypes) {
			super(ConfigCategoryGui.this, Minecraft.getMinecraft());
			if (configTypes.length == 0) {
				return;
			}
			if (configTypes.length == 1) {
				Arrays.stream(ConfigUtil.getConfigFields(configTypes[0], true))
						.sorted(Comparator.comparing(Field::getGenericType, Comparator.comparing(ConfigUtil::isNonMapCategory).reversed())
								.thenComparingInt(f -> f.isAnnotationPresent(Order.class) ? f.getAnnotation(Order.class).value() : 0)
								.thenComparing(f -> f.isAnnotationPresent(Config.Name.class) ? f.getAnnotation(Config.Name.class).value() : f.getName()))
						.forEach(field -> {
							this.listEntries.add(this.create(null, field));
						});
			} else {
				for (Class<?> type : configTypes) {
					this.listEntries.add(new ConfigCategoryGuiEntry(this, type));
				}
			}
		}

		public Entries(Type type, @Nullable Object instance) {
			super(ConfigCategoryGui.this, Minecraft.getMinecraft());
			Arrays.stream(ConfigUtil.getConfigFields(type, instance == null))
					.sorted(Comparator.comparing(Field::getGenericType, Comparator.comparing(ConfigUtil::isNonMapCategory).reversed())
							.thenComparingInt(f -> f.isAnnotationPresent(Order.class) ? f.getAnnotation(Order.class).value() : 0)
							.thenComparing(f -> f.isAnnotationPresent(Config.Name.class) ? f.getAnnotation(Config.Name.class).value() : f.getName()))
					.forEach(field -> {
						this.listEntries.add(this.create(instance, field));
					});
		}

		private IConfigEntry create(@Nullable Object instance, Field field) {
			return new ConfigCategoryGuiEntry(this, instance, field);
		}

		@Override
		public boolean areAnyEntriesEnabled(boolean includeChildren) {
			return this.listEntries.stream()
					.map(ConfigCategoryGuiEntry.class::cast)
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.CategoryEntry))
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.ListEntry))
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.MapEntry))
					.anyMatch(IConfigEntry::enabled);
		}

		@Override
		public boolean areAllEntriesDefault(boolean includeChildren) {
			return this.listEntries.stream()
					.map(ConfigCategoryGuiEntry.class::cast)
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.CategoryEntry))
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.ListEntry))
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.MapEntry))
					.allMatch(IConfigEntry::isDefault);
		}

		@Override
		public void setAllToDefault(boolean includeChildren) {
			this.listEntries.stream()
					.map(ConfigCategoryGuiEntry.class::cast)
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.CategoryEntry))
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.ListEntry))
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.MapEntry))
					.forEach(IConfigEntry::setToDefault);
		}

		@Override
		public boolean hasChangedEntry(boolean includeChildren) {
			return this.listEntries.stream()
					.map(ConfigCategoryGuiEntry.class::cast)
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.CategoryEntry))
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.ListEntry))
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.MapEntry))
					.anyMatch(IConfigEntry::isChanged);
		}

		@Override
		public void undoAllChanges(boolean includeChildren) {
			this.listEntries.stream()
					.map(ConfigCategoryGuiEntry.class::cast)
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.CategoryEntry))
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.ListEntry))
					.filter(e -> includeChildren || !(e.entry instanceof meldexun.betterconfig.gui.entry.MapEntry))
					.forEach(IConfigEntry::undoChanges);
		}

		@Override
		public int getListWidth() {
			return ConfigCategoryGui.this.width - 4;
		}

		@Override
		public int offsetLeft() {
			return 0;
		}

	}

}
