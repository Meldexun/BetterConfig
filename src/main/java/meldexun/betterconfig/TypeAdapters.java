package meldexun.betterconfig;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

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

		TypeAdapters.registerString(v -> v.x + "," + v.y, s -> {
			String[] a = s.split(",");
			if (a.length != 2) throw new IllegalArgumentException();
			return new Vector2f(
					Float.parseFloat(a[0].trim()),
					Float.parseFloat(a[1].trim()));
		}, "0,0", Vector2f.class);
		TypeAdapters.registerString(v -> v.x + "," + v.y + "," + v.z, s -> {
			String[] a = s.split(",");
			if (a.length != 3) throw new IllegalArgumentException();
			return new Vector3f(
					Float.parseFloat(a[0].trim()),
					Float.parseFloat(a[1].trim()),
					Float.parseFloat(a[2].trim()));
		}, "0,0,0", Vector3f.class);
		TypeAdapters.registerString(v -> v.x + "," + v.y + "," + v.z + "," + v.w, s -> {
			String[] a = s.split(",");
			if (a.length != 4) throw new IllegalArgumentException();
			return new Vector4f(
					Float.parseFloat(a[0].trim()),
					Float.parseFloat(a[1].trim()),
					Float.parseFloat(a[2].trim()),
					Float.parseFloat(a[3].trim()));
		}, "0,0,0,0", Vector4f.class);
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
