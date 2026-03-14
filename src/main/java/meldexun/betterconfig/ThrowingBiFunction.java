package meldexun.betterconfig;

interface ThrowingBiFunction<T, U, E extends Exception> {

	boolean apply(T t, U u) throws E;

}
