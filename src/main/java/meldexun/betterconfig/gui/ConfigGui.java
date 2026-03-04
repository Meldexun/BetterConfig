package meldexun.betterconfig.gui;

import meldexun.betterconfig.api.BetterConfig;

public interface ConfigGui {

	BetterConfig settings();

	void recalculateState();

}
