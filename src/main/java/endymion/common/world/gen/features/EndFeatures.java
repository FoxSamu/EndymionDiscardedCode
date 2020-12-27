package endymion.common.world.gen.features;

import com.google.common.collect.Sets;
import endymion.common.block.EndBlocks;
import endymion.common.world.gen.placer.OffsetBlockPlacer;
import endymion.common.world.gen.placer.SelectiveBlockPlacer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;

public final class EndFeatures {
    public static final ConfiguredFeature<?, ?> PATCH_SHADE_GRASS = register(
        "endymion:patch_shade_grass",
        Feature.RANDOM_PATCH.configure(Configs.SHADE_GRASS_CONFIG)
                            .decorate(Features.Placements.SQUARE_HEIGHTMAP_SPREAD_DOUBLE)
                            .decorate(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 20, 30)))
    );
    public static final ConfiguredFeature<?, ?> PATCH_SHALIMON = register(
        "endymion:patch_shalimon",
        Feature.RANDOM_PATCH.configure(Configs.SHALIMON_CONFIG)
                            .decorate(Placement.HEIGHTMAP.configure(NoPlacementConfig.INSTANCE))
                            .decorate(Placement.CHANCE.configure(new ChanceConfig(4)))
    );
    public static final ConfiguredFeature<?, ?> PATCH_SHADE_LIGHT = register(
        "endymion:patch_shade_light",
        Feature.RANDOM_PATCH.configure(Configs.SHADE_LIGHT_CONFIG)
                            .decorate(Placement.HEIGHTMAP.configure(NoPlacementConfig.INSTANCE))
                            .decorate(Placement.CHANCE.configure(new ChanceConfig(7)))
    );
    public static final ConfiguredFeature<?, ?> PATCH_END_ROOTS = register(
        "endymion:patch_end_roots",
        Feature.RANDOM_PATCH.configure(Configs.END_ROOTS_CONFIG)
                            .decorate(Placement.HEIGHTMAP.configure(NoPlacementConfig.INSTANCE))
                            .decorate(Placement.CHANCE.configure(new ChanceConfig(2)))
    );
    public static final ConfiguredFeature<?, ?> DEPOSIT_SHADELITH = register(
        "endymion:deposit_shadelith",
        EndFeature.RANDOM_DEPO.configure(Configs.SHADELITH_CONFIG)
                              .decorate(Placement.HEIGHTMAP.configure(NoPlacementConfig.INSTANCE))
                              .decorate(Placement.CHANCE.configure(new ChanceConfig(8)))
    );

    public static final ConfiguredFeature<?, ?> LUMRUD_FUNGUS = register(
        "lumrud_fungus",
        Feature.HUGE_FUNGUS.configure(Configs.LUMRUD_FUNGUS_NOT_PLANTED_CONFIG)
                           .decorate(Placement.COUNT_MULTILAYER.configure(new FeatureSpreadConfig(8))));
    public static final ConfiguredFeature<HugeFungusConfig, ?> LUMRUD_FUNGUS_PLANTED = register(
        "lumrud_fungus_planted",
        Feature.HUGE_FUNGUS.configure(Configs.LUMRUD_FUNGUS_CONFIG)
    );

    private EndFeatures() {
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> feature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, name, feature);
    }

    public static final class Configs {
        public static final BlockClusterFeatureConfig SHADE_GRASS_CONFIG = new BlockClusterFeatureConfig.Builder(
            new SimpleBlockStateProvider(States.SHADE_GRASS),
            new SimpleBlockPlacer()
        ).tries(84).cannotProject().build();

        public static final BlockClusterFeatureConfig SHALIMON_CONFIG = new BlockClusterFeatureConfig.Builder(
            new SimpleBlockStateProvider(States.SHALIMON),
            new SelectiveBlockPlacer(SelectiveBlockPlacer.SHALIMON, new SimpleBlockPlacer())
        ).tries(64).cannotProject().build();

        public static final BlockClusterFeatureConfig END_ROOTS_CONFIG = new BlockClusterFeatureConfig.Builder(
            new SimpleBlockStateProvider(States.END_ROOTS),
            new SimpleBlockPlacer()
        ).tries(64).cannotProject().build();

        public static final BlockClusterFeatureConfig SHADE_LIGHT_CONFIG = new BlockClusterFeatureConfig.Builder(
            new SimpleBlockStateProvider(States.SHADE_LIGHT),
            new OffsetBlockPlacer(new SimpleBlockPlacer(), 0, -1, 0)
        ).tries(56).cannotProject().whitelist(Sets.newHashSet(
            EndBlocks.SHADE_DIRT,
            EndBlocks.SHADE_GRASS_BLOCK,
            EndBlocks.SHADELITH
        )).build();

        public static final RandomDepositConfig SHADELITH_CONFIG = new RandomDepositConfig(
            new SimpleBlockStateProvider(States.SHADELITH),
            1, 3, 3, 3, 3, 2
        );

        public static final HugeFungusConfig LUMRUD_FUNGUS_CONFIG = new HugeFungusConfig(
            EndBlocks.SHADE_GRASS_BLOCK.getDefaultState(),
            EndBlocks.LUMRUD_STEM.getDefaultState(),
            EndBlocks.LUMRUD_SPORE_BLOCK.getDefaultState(),
            EndBlocks.SHADE_LIGHT.getDefaultState(),
            true
        );

        public static final HugeFungusConfig LUMRUD_FUNGUS_NOT_PLANTED_CONFIG = new HugeFungusConfig(
            LUMRUD_FUNGUS_CONFIG.validBaseBlock,
            LUMRUD_FUNGUS_CONFIG.stemState,
            LUMRUD_FUNGUS_CONFIG.hatState,
            LUMRUD_FUNGUS_CONFIG.decorationState,
            false
        );

        private Configs() {
        }
    }

    public static final class States {
        static final BlockState SHADE_GRASS = EndBlocks.SHADE_GRASS.getDefaultState();
        static final BlockState END_ROOTS = EndBlocks.END_ROOTS.getDefaultState();
        static final BlockState SHALIMON = EndBlocks.SHALIMON.getDefaultState();
        static final BlockState FLOWERED_SHALIMON = EndBlocks.FLOWERED_SHALIMON.getDefaultState();
        static final BlockState SHADELITH = EndBlocks.SHADELITH.getDefaultState();
        static final BlockState SHADE_LIGHT = EndBlocks.SHADE_LIGHT.getDefaultState();

        private States() {
        }
    }
}
