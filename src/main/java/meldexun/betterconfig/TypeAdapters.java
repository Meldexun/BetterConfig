package meldexun.betterconfig;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import net.minecraft.util.ResourceLocation;

public class TypeAdapters {

	private static final Map<Type, TypeAdapter<?>> TYPE_ADAPTERS = new ConcurrentHashMap<>();

	static {
		register(Object::toString, Boolean::valueOf, false, boolean.class, Boolean.class);
		register(Object::toString, Byte::valueOf, (byte) 0, byte.class, Byte.class);
		register(Object::toString, Short::valueOf, (short) 0, short.class, Short.class);
		register(Object::toString, Integer::valueOf, 0, int.class, Integer.class);
		register(Object::toString, Long::valueOf, 0L, long.class, Long.class);
		register(Object::toString, Float::valueOf, 0.0F, float.class, Float.class);
		register(Object::toString, Double::valueOf, 0.0D, double.class, Double.class);
		register(Integer::toString, s -> (char) Integer.parseInt(s), (char) 0, char.class, Character.class);
		register(Function.identity(), Function.identity(), "", String.class);
		// TODO delay registration to avoid early class loading
		register(ResourceLocation::toString, ResourceLocation::new, new ResourceLocation("unkown"), ResourceLocation.class);
	}

	@SafeVarargs
	public static <T> void register(Function<T, String> serializer, Function<String, T> deserializer, T defaultValue, Type... types) {
		TypeAdapter<T> adapter = TypeAdapter.create(serializer, deserializer, defaultValue);
		for (Type type : types) {
			register(type, adapter);
		}
	}

	@SafeVarargs
	public static <T> void registerString(Function<T, String> serializer, Function<String, T> deserializer, String defaultValue, Type... types) {
		TypeAdapter<T> adapter = TypeAdapter.create(serializer, deserializer, defaultValue);
		for (Type type : types) {
			register(type, adapter);
		}
	}

	public static <T> void register(Function<T, String> serializer, Function<String, T> deserializer, T defaultValue, Type type) {
		register(type, TypeAdapter.create(serializer, deserializer, defaultValue));
	}

	public static <T> void registerString(Function<T, String> serializer, Function<String, T> deserializer, String defaultValue, Type type) {
		register(type, TypeAdapter.create(serializer, deserializer, defaultValue));
	}

	public static <T> void register(Type type, TypeAdapter<T> typeAdapter) {
		if (TYPE_ADAPTERS.containsKey(type)) {
			throw new IllegalArgumentException();
		}
		TYPE_ADAPTERS.put(type, typeAdapter);
	}

	@SuppressWarnings("unchecked")
	public static <T> TypeAdapter<T> get(Type type) {
		return (TypeAdapter<T>) TYPE_ADAPTERS.computeIfAbsent(type, k -> {
			if (TypeUtil.isEnum(k)) {
				return createEnumAdapter((Class<Enum<?>>) k);
			}
			return null;
		});
	}

	@SuppressWarnings("unchecked")
	private static <E extends Enum<E>> TypeAdapter<E> createEnumAdapter(Type type) {
		E[] values = TypeUtil.getEnumConstants(type);
		if (values.length == 0) {
			return TypeAdapter.<E>create(Enum::name, s -> Enum.valueOf((Class<E>) type, s), "");
		}
		return TypeAdapter.create(Enum::name, s -> Enum.valueOf((Class<E>) type, s), values[0]);
	}

	public static boolean hasAdapter(Type type) {
		return TypeUtil.isEnum(type) || TYPE_ADAPTERS.containsKey(TypeUtil.getRawType(type));
	}

}
