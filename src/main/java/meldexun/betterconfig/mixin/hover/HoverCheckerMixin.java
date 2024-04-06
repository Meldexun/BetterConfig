package meldexun.betterconfig.mixin.hover;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraftforge.fml.client.config.HoverChecker;

@Mixin(value = HoverChecker.class, remap = false)
public class HoverCheckerMixin {

	@Shadow(remap = false)
	private int bottom;
	@Shadow(remap = false)
	private int right;

	@ModifyVariable(method = "checkHover(IIZ)Z", remap = false, at = @At(value = "FIELD", target = "Lnet/minecraftforge/fml/client/config/HoverChecker;right:I", remap = false, opcode = Opcodes.PUTFIELD, shift = Shift.AFTER), index = 3, order = 0, name = "arg2")
	public boolean fixBounds(boolean canHover) {
		this.bottom--;
		this.right--;
		return canHover;
	}

}
