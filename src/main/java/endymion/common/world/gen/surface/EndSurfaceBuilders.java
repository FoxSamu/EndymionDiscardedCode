package endymion.common.world.gen.surface;

import endymion.common.block.EndBlocks;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public final class EndSurfaceBuilders {
    public static final SurfaceBuilderConfig SHADE_CONFIG = new SurfaceBuilderConfig(
        EndBlocks.SHADE_GRASS_BLOCK.getDefaultState(),
        EndBlocks.SHADE_DIRT.getDefaultState(),
        EndBlocks.SHADE_DIRT.getDefaultState()
    );

    private EndSurfaceBuilders() {
    }
}
