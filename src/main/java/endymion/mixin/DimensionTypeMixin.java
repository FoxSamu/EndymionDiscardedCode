package endymion.mixin;

import endymion.common.world.biome.provider.EndymionBiomeProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public abstract class DimensionTypeMixin {

    @Inject(method = "createEndGenerator", at = @At("RETURN"), cancellable = true)
    private static void onCreateEndGenerator(Registry<Biome> biomes, Registry<DimensionSettings> configs, long seed, CallbackInfoReturnable<ChunkGenerator> d) {
        NoiseChunkGenerator out = new NoiseChunkGenerator(
            new EndymionBiomeProvider(biomes, seed),
            seed,
            () -> configs.getOrThrow(DimensionSettings.END)
        );
        d.setReturnValue(out);
    }
}
