package meldexun.betterconfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

public class AnnotationUtil {

	public static boolean isPresent(AnnotatedElement type, Class<? extends Annotation> annotation) {
		return type.isAnnotationPresent(annotation);
	}

	public static <T extends Annotation> T get(AnnotatedElement type, Class<T> annotation) {
		return type.getAnnotation(annotation);
	}

	public static <T extends Annotation> void ifPresent(AnnotatedElement type, Class<T> annotation, Consumer<T> action) {
		if (isPresent(type, annotation)) {
			action.accept(get(type, annotation));
		}
	}

	@Nullable
	public static <T extends Annotation, R> R map(AnnotatedElement type, Class<T> annotation, Function<T, R> mappingFunction) {
		return map(type, annotation, mappingFunction, null);
	}

	public static <T extends Annotation, R> R map(AnnotatedElement type, Class<T> annotation, Function<T, R> mappingFunction, R defaultValue) {
		if (isPresent(type, annotation)) {
			return mappingFunction.apply(get(type, annotation));
		}
		return defaultValue;
	}

	public static boolean isPresent(Type type, Class<? extends Annotation> annotation) {
		return isPresent((AnnotatedElement) TypeUtil.getRawType(type), annotation);
	}

	public static <T extends Annotation> T get(Type type, Class<T> annotation) {
		return get((AnnotatedElement) TypeUtil.getRawType(type), annotation);
	}

	public static <T extends Annotation> void ifPresent(Type type, Class<T> annotation, Consumer<T> action) {
		ifPresent((AnnotatedElement) TypeUtil.getRawType(type), annotation, action);
	}

	@Nullable
	public static <T extends Annotation, R> R map(Type type, Class<T> annotation, Function<T, R> mappingFunction) {
		return map((AnnotatedElement) TypeUtil.getRawType(type), annotation, mappingFunction);
	}

	public static <T extends Annotation, R> R map(Type type, Class<T> annotation, Function<T, R> mappingFunction, R defaultValue) {
		return map((AnnotatedElement) TypeUtil.getRawType(type), annotation, mappingFunction, defaultValue);
	}

}
