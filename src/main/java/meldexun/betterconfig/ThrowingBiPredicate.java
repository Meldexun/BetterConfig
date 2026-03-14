package meldexun.betterconfig;

interface ThrowingBiPredicate<T, U, E extends Exception> {

	boolean apply(T t, U u) throws E;

}
