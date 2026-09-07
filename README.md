# Better Config
[![Maven Central](https://img.shields.io/maven-central/v/io.github.meldexun/betterconfig)](https://central.sonatype.com/artifact/io.github.meldexun/betterconfig)

BetterConfig extends Forge's annotation-based config system to support nested arrays, collections, arrays/collections/maps of objects, and much more.

## Usage
BetterConfig is hosted on [Maven Central](https://central.sonatype.com/artifact/io.github.meldexun/betterconfig). To get started, add the following to your dependencies in your build script:
```
implementation fg.deobf('io.github.meldexun:betterconfig:1.12.2-1.1.1')
```

To migrate from Forge to BetterConfig, you only have to
- annotate your config class with `@BetterConfig` instead of `@Config` and
- replace all invocations of `ConfigManager.sync(String, Type)` with `BetterConfigManager.sync(String)`.

That's it! Now your config will be processed by BetterConfig.

## Features
Everything that the Forge annotation config system supports and more. Every new feature will be explained below.
- **Collections**<br>
  Most kinds of collections are supported natively.
  ```Java
  public static List<String> list = new ArrayList<>();
  public static Set<String> set = new LinkedHashSet<>();
  ```
  LIMITATIONS: Either the type of a field's value or the field's type must be non-abstract with a public no-argument constructor!
  It's strongly recommended to only use collection implementations with predictable iteration order, like List or LinkedHashSet. Otherwise, entries might get shuffled around unexpectedly.
- **Arrays/Collections/Maps of categories**<br>
  All non-abstract types (similar to sub-categories) can be used as array, collection, and map values.
  ```Java
  public static class DataClass {
      public int value = 1234;
  }

  public static DataClass[] array = ...;
  public static List<DataClass> list = ...;
  public static Map<String, DataClass> map = ...;
  ```
- **Nested Arrays/Collections/Maps**<br>
  Nested arrays, collections, and maps are supported natively.
  ```Java
  public static String[][] array_of_arrays = ...;
  public static ArrayList<String>[] array_of_lists = ...;
  public static LinkedHashMap<String, String>[] array_of_maps = ...;
  
  public static List<String[]> list_of_arrays = ...;
  public static List<ArrayList<String>> list_of_lists = ...;
  public static List<LinkedHashMap<String, String>> list_of_maps = ...;
  
  public static Map<String, String[]> map_of_arrays = ...;
  public static Map<String, ArrayList<String>> map_of_lists = ...;
  public static Map<String, LinkedHashMap<String, String>> map_of_maps = ...;
  ```
  LIMITATIONS: The component type of arrays/element type of collections/value type of maps must be a value type or non-abstract with a public no-argument constructor!
- **Category inheritance**<br>
  Category types may inherit entries from their superclass.
  ```Java
  public static class GenerationSettings {
      public int chance = 10;
  }
  public static class TreeGenerationSettings extends GenerationSettings {
      public int size = 8;
  }

  public static TreeGenerationSettings treeSettings = new TreeGenerationSettings();
  ```
- **More value types**<br>
  BetterConfig adds native support for the following types
  - `net.minecraft.util.ResourceLocation`*
  - `net.minecraft.util.math.Vec3i`*
  - `net.minecraft.util.math.Vec3d`*
  - `net.minecraft.util.math.BlockPos`*
  - `org.lwjgl.util.vector.Vector2f`
  - `org.lwjgl.util.vector.Vector3f`
  - `org.lwjgl.util.vector.Vector4f`
  
  \* To prevent very early loading of vanilla classes, these types are *not* supported for configs that are loaded early via `@LoadEarly`.
- **More map key types**<br>
  All value types are supported as map keys.
- **GUI-Extensible Maps**<br>
  Maps are by default extensible when editing using the in-game GUI.<br>
  To make a map unmodifiable through the in-game GUI again, use `@Unmodifiable`.
- **`@Order`**<br>
  This annotation can be added to fields to specify an explicit order which will be used when writing the config to file and when using the in-game GUI.
- **`@RangeLong`**<br>
  Forge only has `@RangeInt`, which may not be enough in some cases. Fields with type long may be annotated with this annotation to specify their minimum and maximum value.
- **`@Unmodifiable`**<br>
  Fields whose type is an array, collection, or map may be annotated with this annotation to prevent users from adding or removing entries through the in-game GUI. This does not prevent users from adding or removing entries from the config file! Thus, this annotation should not be relied on for some element has an expected number of entries.
- **`@LoadEarly`**<br>
  Configs annotated with this annotation will be loaded immediately when the class gets initialized. The class must not be excluded from the class transformation pipeline! The class must not be loaded before the BetterConfigPlugin is created!
- **`@Sync`**<br>
  When a player logs in/a config is changed, all configs annotated with this annotation will automatically be sent to that player/all players.
- Additional `@BetterConfig` settings
  - **version**<br>
    If non-empty, the version will be written to the config file as `~CONFIG_VERSION(<CLASS_NAME>): <VERSION>`. The config file's version will be available in the `@BetterConfig.AfterRead` callback, allowing for migration of old configs.
  - **lowerCaseCategories**<br>
    If true, category names will always be lowercase, as they are in Forge.
  - **bigCategoryComments**<br>
    If true, category comments will be guarded, as they are in Forge. Otherwise, category comments will be formatted like value comments.
    
    bigCategoryComments = true
    ```
    ##########################################################################################################
    # defaults
    #--------------------------------------------------------------------------------------------------------#
    # Default configuration for forge chunk loading control
    ##########################################################################################################

    defaults { ...
    ```

    bigCategoryComments = false
    ```
    # Default configuration for forge chunk loading control
    defaults { ...
    ```
  - **addRangesToComments**<br>
    If true, the range of number-fields, if specified, will be written to the config file.
  - **addDefaultsToComments**<br>
    If true, the defaults of value-, list-, and map-fields will be written to the config file.
  - **removeDeprecatedEntries**<br>
    If true, entries only present in the config file will be removed. Otherwise, these deprecated entries will be marked with `~Deprecated~`.
  - **elementOrder**<br>
    Specifies the order in which entries will be sorted. Comparators are applied sequentially. If a comparator considers two elements equal, the next comparator is used to determine the order.
    - `EXPLICIT` - Orders elements by the value of their `@Order` annotation.
    - `CATEGORIES_FIRST` - Category elements come first.
    - `CATEGORIES_LAST` - Category elements come last.
    - `NON_MAP_CATEGORIES_FIRST` – Non-map-category elements come first.
    - `NON_MAP_CATEGORIES_LAST` – Non-map-category elements come last.
    - `NAME_CASE_SENSITIVE` – Orders elements by comparing their name lexicographically, case-sensitively.
    - `NAME_CASE_INSENSITIVE` – Orders elements by comparing their name lexicographically, ignoring case.
    - `INITIALIZATION` – **WARNING, READ CAREFULLY!** Attempts to order elements by their initialization order. The JVM does not provide a guaranteed way to retrieve field initialization order at runtime. BetterConfig analyzes the class bytecode to approximate this order. While this works in most cases, correctness and stability are not guaranteed.
- **Callbacks**<br>
  A callback is a public static method in a config class that the config system invokes automatically at a specific point in the config lifecycle. Each callback type is identified by its own annotation and has its own required method signature
  - **`@BetterConfig.AfterRead`**
    This callback is called after a config file is read but before the data gets loaded into the fields of the config class. This callback can be used in conjunction with the version property of the `@BetterConfig` annotation to migrate old configs without end-users having to do anything.<br>
    Use the context to create new categories, lists, and values.<br>
    Note that the version may be null for reasons other than the config being older than when versioning was introduced, such as corrupted or malformed config files.
    ```Java
    @BetterConfig.AfterRead
    public static <T extends IConfigContext<T>> void afterConfigRead(IConfigCategory<T> config, T context, @Nullable ArtifactVersion version) {
        // ...
    }
    ```

## Mod Features
*Features for players installing the mod*
- In-game GUI editing of mods using `net.minecraftforge.common.config.Configuration`. These configs would normally not be editable in-game because they don't use Forge's annotation-based config system.
  Depending on how these mods use their config, you might have to restart your game to see an effect.
