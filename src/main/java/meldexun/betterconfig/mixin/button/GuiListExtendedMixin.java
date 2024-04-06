package meldexun.betterconfig.mixin.button;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import meldexun.betterconfig.IGuiListEntryExtended;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;

@Mixin(GuiListExtended.class)
public class GuiListExtendedMixin {

	@Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiListExtended$IGuiListEntry;mousePressed(IIIIII)Z"))
	private boolean mousePressed(IGuiListEntry entry, int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
		return ((IGuiListEntryExtended) entry).mousePressedAll(slotIndex, mouseX, mouseY, mouseEvent, relativeX, relativeY);
	}

}
