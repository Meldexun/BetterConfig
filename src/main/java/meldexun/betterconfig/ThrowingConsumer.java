package meldexun.betterconfig;

interface ThrowingConsumer<T, E extends Exception> {

	void accept(T t) throws E;

}
