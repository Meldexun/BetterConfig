package meldexun.betterconfig.gui.entry;

import java.lang.reflect.Type;

import org.apache.commons.lang3.reflect.TypeUtils;

import meldexun.betterconfig.ConfigElementMetadata;

public class LongEntry extends StringEntry {

	public LongEntry(ConfigElementMetadata metadata, Type type, Object beforeValue) {
		super(metadata, type, beforeValue);
		if (!TypeUtils.isAssignable(this.type, byte.class)
				&& !TypeUtils.isAssignable(this.type, short.class)
				&& !TypeUtils.isAssignable(this.type, int.class)
				&& !TypeUtils.isAssignable(this.type, long.class)
				&& !TypeUtils.isAssignable(this.type, char.class)) {
			throw new IllegalArgumentException();
		}
		if (this.metadata.hasLongRange()) {
			this.validator = this.validator.and(s -> {
				long l = Long.parseLong(s);
				return l >= this.metadata.minLong() && l <= this.metadata.maxLong();
			});
		}
	}

}
