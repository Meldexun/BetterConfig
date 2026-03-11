package meldexun.betterconfig.gui.entry;

import java.lang.reflect.Type;

import org.apache.commons.lang3.reflect.TypeUtils;

import meldexun.betterconfig.ConfigElementMetadata;

public class DoubleEntry extends StringEntry {

	public DoubleEntry(ConfigElementMetadata metadata, Type type, Object beforeValue) {
		super(metadata, type, beforeValue);
		if (!TypeUtils.isAssignable(this.type, float.class)
				&& !TypeUtils.isAssignable(this.type, double.class)) {
			throw new IllegalArgumentException();
		}
		if (this.metadata.hasDoubleRange()) {
			this.validator = this.validator.and(s -> {
				double d = Double.parseDouble(s);
				return d >= this.metadata.minDouble() && d <= this.metadata.maxDouble();
			});
		}
	}

}
