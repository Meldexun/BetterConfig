package meldexun.betterconfig;

interface ThrowingConsumer<T, E extends Exception> {

	void accept(T t) throws E;

	static <T, E extends Exception> ThrowingConsumer<T, E> noop() {
		return t -> {};
	}

}
