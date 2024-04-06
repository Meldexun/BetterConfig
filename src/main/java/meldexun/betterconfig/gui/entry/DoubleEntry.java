package meldexun.betterconfig.gui.entry;

import java.lang.reflect.Type;

import org.apache.commons.lang3.reflect.TypeUtils;

import meldexun.betterconfig.gui.EntryInfo;

public class DoubleEntry extends StringEntry {

	public DoubleEntry(EntryInfo info, Type type, Object beforeValue) {
		super(info, type, beforeValue);
		if (!TypeUtils.isAssignable(this.type, float.class)
				&& !TypeUtils.isAssignable(this.type, double.class)) {
			throw new IllegalArgumentException();
		}
		if (this.info.hasDoubleRange()) {
			this.validator = this.validator.and(s -> {
				double d = Double.parseDouble(s);
				return d >= this.info.minDouble() && d <= this.info.maxDouble();
			});
		}
	}

}
