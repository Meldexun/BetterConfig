package meldexun.betterconfig.mixin.button;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import meldexun.betterconfig.gui.IGuiListEntryExt;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;

@Mixin(IGuiListEntry.class)
public interface IGuiListEntryMixin extends IGuiListEntryExt {

	@Override
	default boolean mousePressedAll(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
		return mouseEvent == 0 && mousePressed(slotIndex, mouseX, mouseY, mouseEvent, relativeX, relativeY);
	}

	@Shadow
	boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY);

}
