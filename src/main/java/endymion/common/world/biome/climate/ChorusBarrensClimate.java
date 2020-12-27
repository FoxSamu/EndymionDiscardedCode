package endymion.common.world.biome.climate;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

public class ChorusBarrensClimate implements EndClimate {
    private final Biome highlands;
    private final Biome midlands;
    private final Biome smallIslands;
    private final Biome barrens;

    public ChorusBarrensClimate(Biome highlands, Biome midlands, Biome smallIslands, Biome barrens) {
        this.highlands = highlands;
        this.midlands = midlands;
        this.smallIslands = smallIslands;
        this.barrens = barrens;
    }

    @Override
    public Biome[] getBiomes() {
        return new Biome[] {highlands, midlands, smallIslands, barrens};
    }

    @Override
    public Biome getBiome(int x, int y, int z, double noise) {
        if (noise > 40) {
            return highlands;
        } else if (noise >= 0) {
            return midlands;
        } else {
            return noise < -20 ? barrens : smallIslands;
        }
    }

    @Override
    public double getDensity(int x, int z) {
        return 0.1;
    }

    @Override
    public double getDepth(int x, int z, double noise) {
        return noise - 8;
    }

    @Override
    public double getScaleFactor(int x, int z, double noise) {
        return noise > 8 ? 0.25 : 1;
    }

    @Override
    public float getIslandRandomness(int x, int z) {
        return (MathHelper.abs(x) * 3439 + MathHelper.abs(z) * 147) % 13 + 9;
    }
}
