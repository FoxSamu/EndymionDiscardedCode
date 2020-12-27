package endymion.common.world.biome;

import endymion.common.world.gen.features.EndFeature;
import endymion.common.world.gen.features.EndFeatures;
import endymion.common.world.gen.surface.EndConfiguredSurfaceBuilders;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Features;

public final class EndBiomeMaker {

    private EndBiomeMaker() {
    }

    private static Biome composeEndSpawnSettings(BiomeGenerationSettings.Builder genSettings) {
        MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();
        DefaultBiomeFeatures.addEndMobs(spawnInfo);
        return new Biome.Builder().precipitation(Biome.RainType.NONE)
                                  .category(Biome.Category.THEEND)
                                  .depth(0.2F)
                                  .scale(0.4F)
                                  .temperature(0.5F)
                                  .downfall(0.5F)
                                  .effects(new BiomeAmbience.Builder()
                                               .waterColor(0x3F76E4)
                                               .waterFogColor(0x050533)
                                               .fogColor(0x003555)
                                               .skyColor(0x000000)
                                               .moodSound(MoodSoundAmbience.CAVE)
                                               .build()
                                  )
                                  .spawnSettings(spawnInfo.build())
                                  .generationSettings(genSettings.build())
                                  .build();
    }

    public static Biome createShadePlains() {
        BiomeGenerationSettings.Builder genSettings
            = new BiomeGenerationSettings.Builder()
                  .surfaceBuilder(EndConfiguredSurfaceBuilders.SHADE)
                  .feature(GenerationStage.Decoration.SURFACE_STRUCTURES, Features.END_GATEWAY)
                  .feature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, EndFeatures.PATCH_SHADE_LIGHT)
                  .feature(GenerationStage.Decoration.RAW_GENERATION, EndFeatures.DEPOSIT_SHADELITH)
                  .feature(GenerationStage.Decoration.VEGETAL_DECORATION, EndFeatures.PATCH_SHADE_GRASS)
                  .feature(GenerationStage.Decoration.VEGETAL_DECORATION, EndFeatures.PATCH_END_ROOTS)
                  .feature(GenerationStage.Decoration.VEGETAL_DECORATION, EndFeatures.LUMRUD_FUNGUS)
                  .feature(GenerationStage.Decoration.VEGETAL_DECORATION, EndFeatures.PATCH_SHALIMON);
        return composeEndSpawnSettings(genSettings);
    }

    public static Biome createShadeVoid() {
        BiomeGenerationSettings.Builder genSettings
            = new BiomeGenerationSettings.Builder()
                  .surfaceBuilder(EndConfiguredSurfaceBuilders.SHADE)
                  .feature(GenerationStage.Decoration.VEGETAL_DECORATION, EndFeatures.PATCH_SHADE_GRASS);
        return composeEndSpawnSettings(genSettings);
    }
}
