package endymion.mixin;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EndBiomeProvider.class)
public abstract class EndBiomeProviderMixin {

    private static boolean checkDistance(int x, int z, int limit) {
        return Math.abs(x) <= limit && Math.abs(z) <= limit && x * x + z * z <= limit * limit;
    }

    /**
     * @author Shadew
     * @reason Fixes a critical levelgen bug in the End
     */
    @Overwrite
    public static float getNoiseAt(SimplexNoiseGenerator noise, int x, int z) { // Input coords are quarterchunk coords
        // Chunk coordinates
        int cx = x / 2;
        int cz = z / 2;

        // Chunk-local coordinates
        int lx = x % 2;
        int lz = z % 2;


        float islandFactor = -100;

        // Generate main island (fix MC-159283)
        if (Math.abs(x) < 200 && Math.abs(z) < 200) {
            islandFactor = 100 - MathHelper.sqrt(x * x + z * z) * 8;
            islandFactor = MathHelper.clamp(islandFactor, -100, 80);
        }

        float totalRemoteIslandFactor = -100;

        // Generate remote islands
        for (int rx = -12; rx <= 12; rx++) {
            for (int rz = -12; rz <= 12; rz++) {
                int grx = cx + rx;
                int grz = cz + rz;

                // Check if we can generate a remote island nearby
                // Remote islands are at least 64 chunks (= 1024 blocks) away
                if (!checkDistance(grx, grz, 64) && noise.getValue(grx, grz) < -0.9) {
                    float lrx = lx - rx * 2;
                    float lrz = lz - rz * 2;



                    float randominess = (MathHelper.abs(grx) * 3439 + MathHelper.abs(grz) * 147) % 13 + 9;
                    float remoteIslandFactor = 100 - MathHelper.sqrt(lrx * lrx + lrz * lrz) * randominess;
                    remoteIslandFactor = MathHelper.clamp(remoteIslandFactor, -100, 80);

                    if (remoteIslandFactor > totalRemoteIslandFactor) {
                        totalRemoteIslandFactor = remoteIslandFactor;
                    }
                }
            }
        }


        return Math.max(islandFactor, totalRemoteIslandFactor);
    }
}
