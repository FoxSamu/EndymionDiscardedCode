package endymion.util.mixin;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

public class MixinConnector implements IMixinConnector {
	@Override
	public void connect() {
		InjectionInfo.register(ModifyStackInjectionInfo.class);
		InjectionInfo.register(CustomInjectInjectionInfo.class);
		Mixins.addConfigurations("mixin.endymion.json");
	}
}
