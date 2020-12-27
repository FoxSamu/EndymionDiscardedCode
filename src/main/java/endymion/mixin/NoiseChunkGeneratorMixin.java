package endymion.mixin;

import endymion.common.world.biome.provider.EndymionBiomeProvider;
import endymion.common.world.gen.chunk.IEndymionNoiseChunkGenerator;
import endymion.util.injector.NoiseChunkGeneratorInjector;
import endymion.util.mixin.CustomInjection;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin extends ChunkGenerator implements IEndymionNoiseChunkGenerator {
    @Shadow
    @Final
    protected Supplier<DimensionSettings> settings;

    private final ThreadLocal<double[]> noisebuf = ThreadLocal.withInitial(() -> new double[2]);

    public NoiseChunkGeneratorMixin(BiomeProvider provider, DimensionStructuresSettings structureSettings) {
        super(provider, structureSettings);
    }

    @Override
    public double[] getNoiseBuf() {
        return noisebuf.get();
    }

    @CustomInjection(
        method = "fillNoiseColumn",
        injector = NoiseChunkGeneratorInjector.class,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/biome/provider/EndBiomeProvider;getNoiseAt(Lnet/minecraft/world/gen/SimplexNoiseGenerator;II)F"
        )
    )
    @SuppressWarnings("ALL")
    private static double[] generateProperEndNoise(SimplexNoiseGenerator generator, int x, int z, NoiseChunkGenerator self) {
        BiomeProvider biomeProvider = self.getBiomeProvider();
        if (biomeProvider instanceof EndymionBiomeProvider) {
            return ((EndymionBiomeProvider) biomeProvider).getIslandNoise(generator, x, z, ((IEndymionNoiseChunkGenerator) self).getNoiseBuf());
        }

        // Fallback on old end generator
        float endNoise = EndBiomeProvider.getNoiseAt(generator, x, z);
        double noise = endNoise - 8;
        double scl;
        if(noise > 0) {
            scl = 0.25;
        } else {
            scl = 1;
        }
        return new double[] {noise, scl};
    }

    @Inject(method = "buildSurface", at = @At("RETURN"))
    private void buildSurface(WorldGenRegion world, IChunk chunk, CallbackInfo ci) {
        BiomeProvider biomeProvider = getBiomeProvider();
        if (biomeProvider instanceof EndymionBiomeProvider) {
            ((EndymionBiomeProvider) biomeProvider).processChunk(world, chunk);
        }
    }
}
