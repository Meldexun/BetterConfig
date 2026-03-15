package meldexun.betterconfig.gui;

import org.apache.commons.lang3.StringUtils;

public interface TitledGui {

	String title();

	String subtitle();

	default String subscreen(String subscreen) {
		return !StringUtils.isBlank(this.subtitle()) ? this.subtitle() + " > " + subscreen : subscreen;
	}

}
