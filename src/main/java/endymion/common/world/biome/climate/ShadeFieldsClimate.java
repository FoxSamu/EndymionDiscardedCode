package endymion.common.world.biome.climate;

import endymion.common.block.EndBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;

public class ShadeFieldsClimate implements EndClimate {
    private final Biome fields;
    private final Biome barrens;

    public ShadeFieldsClimate(Biome fields, Biome barrens) {
        this.fields = fields;
        this.barrens = barrens;
    }


    @Override
    public Biome[] getBiomes() {
        return new Biome[] {fields, barrens};
    }

    @Override
    public Biome getBiome(int x, int y, int z, double noise) {
        return noise < -20 ? barrens : fields;
    }

    @Override
    public double getDensity(int x, int z) {
        return 1.6;
    }

    @Override
    public double getDepth(int x, int z, double noise) {
        return 63;
    }

    @Override
    public double getScaleFactor(int x, int z, double noise) {
        return 0.3;
    }

    @Override
    public float getIslandRandomness(int x, int z) {
        return (MathHelper.abs(x) * 3439 + MathHelper.abs(z) * 147) % 13 + 9;
    }

    @Override
    public void replaceBlockState(BlockState state, IChunk chunk, BlockPos.Mutable pos) {
        if(state.isIn(Blocks.END_STONE)) {
            chunk.setBlockState(pos, EndBlocks.SHADELITH.getDefaultState(), false);
        }
    }
}
