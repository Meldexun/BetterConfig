package meldexun.betterconfig;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class DefaultSupplier<T> implements Supplier<T> {

	private T value;
	private T defaultValue;

	private DefaultSupplier(T value, T defaultValue) {
		if (value != null) {
			this.value = value;
		} else {
			this.defaultValue = Objects.requireNonNull(defaultValue);
		}
	}

	public static <T> DefaultSupplier<T> of(T initialValue) {
		return new DefaultSupplier<>(Objects.requireNonNull(initialValue), null);
	}

	public static <T> DefaultSupplier<T> fallback(T fallback) {
		return new DefaultSupplier<>(null, Objects.requireNonNull(fallback));
	}

	public boolean hasValue() {
		return this.value != null;
	}

	@Override
	public T get() {
		if (!this.hasValue()) {
			throw new NoSuchElementException();
		}
		return this.value;
	}

	public T getOrDefault() {
		return this.hasValue() ? this.value : this.defaultValue;
	}

	public void set(T value) {
		this.value = Objects.requireNonNull(value);
		this.defaultValue = null;
	}

	public DefaultSupplier<T> copy() {
		return this.hasValue() ? of(this.value) : fallback(this.defaultValue);
	}

	public <R> DefaultSupplier<R> map(Function<T, R> mappingFunction) {
		return this.hasValue() ? of(mappingFunction.apply(this.value)) : fallback(mappingFunction.apply(this.defaultValue));
	}

	public boolean existsAndNotEqual(T value) {
		return this.hasValue() && !Objects.equals(this.value, value);
	}

}
