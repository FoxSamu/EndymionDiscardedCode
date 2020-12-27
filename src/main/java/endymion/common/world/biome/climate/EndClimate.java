package endymion.common.world.biome.climate;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;

public interface EndClimate {
    Biome[] getBiomes();
    Biome getBiome(int x, int y, int z, double noise);
    double getDensity(int x, int z);
    double getDepth(int x, int z, double noise);
    double getScaleFactor(int x, int z, double noise);
    float getIslandRandomness(int x, int z);
    default void replaceBlockState(BlockState state, IChunk chunk, BlockPos.Mutable pos) {
    }
}
