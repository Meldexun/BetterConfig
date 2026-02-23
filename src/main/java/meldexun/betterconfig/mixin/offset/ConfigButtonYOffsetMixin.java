package meldexun.betterconfig.mixin.offset;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import meldexun.betterconfig.gui.ListEntryBaseExt;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiEditArrayEntries;

@Mixin({ GuiConfigEntries.ListEntryBase.class, GuiEditArrayEntries.BaseEntry.class })
public class ConfigButtonYOffsetMixin implements ListEntryBaseExt {

	@Redirect(method = { "drawEntry(IIIIIIIZF)V", "func_192634_a(IIIIIIIZF)V" }, at = @At(value = "FIELD", target = "Lnet/minecraftforge/fml/client/config/GuiButtonExt;y:I", opcode = Opcodes.PUTFIELD), expect = 2)
	public void setY(GuiButtonExt button, int y) {
		button.y = y + buttonOffsetY();
	}

	@Override
	public int buttonOffsetY() {
		return 0;
	}

}
