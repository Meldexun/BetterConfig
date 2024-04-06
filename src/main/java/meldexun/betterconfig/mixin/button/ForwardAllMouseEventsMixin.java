package meldexun.betterconfig.mixin.button;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiEditArray;

@Mixin(value = { GuiConfig.class, GuiEditArray.class }, remap = false)
public class ForwardAllMouseEventsMixin {

	@Definition(id = "mouseEvent", remap = false, type = int.class, local = @Local(index = 3, ordinal = 2, type = int.class, name = "mouseEvent"))
	@Expression("mouseEvent != 0")
	@ModifyExpressionValue(method = "mouseClicked", remap = false, at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean mouseClicked(boolean mouseEventNotZero) {
		return false;
	}

}
