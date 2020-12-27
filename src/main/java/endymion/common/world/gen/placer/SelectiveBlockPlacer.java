package endymion.common.world.gen.placer;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import endymion.EndymionMod;
import endymion.common.block.EndBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SelectiveBlockPlacer extends BlockPlacer {
    private static final Map<ResourceLocation, IAlterable> REGISTRY = new HashMap<>();
    private static final Map<IAlterable, ResourceLocation> UNREGISTRY = new HashMap<>();
    public static final Codec<SelectiveBlockPlacer> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ResourceLocation.CODEC
                .dispatchMap(
                    "alterable",
                    UNREGISTRY::get,
                    res -> Codec.unit(REGISTRY.get(res))
                )
                .forGetter(placer -> placer.alterable),
            BlockPlacer.TYPE_CODEC
                .fieldOf("placer")
                .forGetter(placer -> placer.placer)
        ).apply(instance, SelectiveBlockPlacer::new)
    );

    public static final IAlterable SHALIMON = register("shalimon", (state, rand) -> {
        if (rand.nextInt(23) < 5) {
            return EndBlocks.FLOWERED_SHALIMON.getDefaultState();
        }
        return state;
    });

    private final IAlterable alterable;
    private final BlockPlacer placer;

    public SelectiveBlockPlacer(IAlterable alterable, BlockPlacer placer) {
        this.alterable = alterable;
        this.placer = placer;
    }

    @Override
    public void generate(IWorld world, BlockPos pos, BlockState state, Random random) {
        BlockState state1 = alterable.alter(state, random);
        placer.generate(world, pos, state1, random);
    }

    @Override
    protected BlockPlacerType<?> getType() {
        return EndBlockPlacerTypes.SELECTIVE;
    }

    private static <T extends IAlterable> T register(String id, T alterable) {
        return register(EndymionMod.resLoc(id), alterable);
    }

    public static <T extends IAlterable> T register(ResourceLocation id, T alterable) {
        REGISTRY.put(id, alterable);
        UNREGISTRY.put(alterable, id);
        return alterable;
    }

    @FunctionalInterface
    public interface IAlterable {
        BlockState alter(BlockState state, Random rand);
    }
}
