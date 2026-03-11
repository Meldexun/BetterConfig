package meldexun.betterconfig;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

class DefaultSupplier<T> implements Supplier<T> {

	private T value;
	private T defaultValue;

	private DefaultSupplier(T value, T defaultValue) {
		if (value != null) {
			this.value = value;
		} else {
			this.defaultValue = Objects.requireNonNull(defaultValue);
		}
	}

	static <T> DefaultSupplier<T> of(T initialValue) {
		return new DefaultSupplier<>(Objects.requireNonNull(initialValue), null);
	}

	static <T> DefaultSupplier<T> fallback(T fallback) {
		return new DefaultSupplier<>(null, Objects.requireNonNull(fallback));
	}

	boolean hasValue() {
		return this.value != null;
	}

	@Override
	public T get() {
		if (!this.hasValue()) {
			throw new NoSuchElementException();
		}
		return this.value;
	}

	T getOrDefault() {
		return this.hasValue() ? this.value : this.defaultValue;
	}

	void set(T value) {
		this.value = Objects.requireNonNull(value);
	}

	void reset() {
		if (this.defaultValue != null) {
			this.value = null;
		}
	}

	DefaultSupplier<T> copy() {
		return this.hasValue() ? of(this.value) : fallback(this.defaultValue);
	}

	<R> DefaultSupplier<R> map(Function<T, R> mappingFunction) {
		return this.hasValue() ? of(mappingFunction.apply(this.value)) : fallback(mappingFunction.apply(this.defaultValue));
	}

	boolean existsAndNotEqual(T value) {
		return this.hasValue() && !Objects.equals(this.value, value);
	}

}
