package meldexun.betterconfig;

import java.util.Objects;
import java.util.function.Function;

public interface TypeAdapter<T> {

	String serialize(T value);

	T deserialize(String s);

	String defaultSerializedValue();

	default T defaultValue() {
		return deserialize(defaultSerializedValue());
	}

	default boolean isSerializedValue(String s) {
		try {
			this.deserialize(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	static <T> TypeAdapter<T> create(Function<T, String> serializer, Function<String, T> deserializer, T defaultValue) {
		Objects.requireNonNull(serializer);
		Objects.requireNonNull(deserializer);
		Objects.requireNonNull(defaultValue);
		return create(serializer, deserializer, serializer.apply(defaultValue));
	}

	static <T> TypeAdapter<T> create(Function<T, String> serializer, Function<String, T> deserializer, String defaultValue) {
		Objects.requireNonNull(serializer);
		Objects.requireNonNull(deserializer);
		Objects.requireNonNull(defaultValue);
		try {
			deserializer.apply(defaultValue);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return new TypeAdapter<T>() {
			@Override
			public String serialize(T value) {
				return serializer.apply(value);
			}

			@Override
			public T deserialize(String s) {
				return deserializer.apply(s);
			}

			@Override
			public String defaultSerializedValue() {
				return defaultValue;
			}
		};
	}

	default T copy(T value) {
		return deserialize(serialize(value));
	}

}
