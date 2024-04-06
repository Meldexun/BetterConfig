package meldexun.betterconfig.mixin.offset;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import meldexun.betterconfig.gui.GuiSlotExt;
import net.minecraft.client.gui.GuiSlot;

@Mixin(GuiSlot.class)
public class GuiSlotMixin implements GuiSlotExt {

	@ModifyConstant(method = "drawScreen", constant = @Constant(intValue = 2, ordinal = 2))
	public int leftOffset(int offset) {
		return offsetLeft();
	}

	@Override
	public int offsetLeft() {
		return 2;
	}

}
