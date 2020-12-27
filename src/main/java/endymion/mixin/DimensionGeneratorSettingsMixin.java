package endymion.mixin;

import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionGeneratorSettings.class)
public abstract class DimensionGeneratorSettingsMixin {
    @Inject(method = "method_28611", at = @At("HEAD"), cancellable = true)
    private void onCheckExperimental(CallbackInfoReturnable<Boolean> info) {
        // This mixin skips the check for modification of the default dimension settings.
        // When the target method returns false, MC shows up a popup that we're using experimental settings. But,
        // this is Modding, it's all experimenting so whoever cares! Just ensure the output is true and the popup
        // doesn't show up by returning true here.
        info.setReturnValue(true);
    }
}
