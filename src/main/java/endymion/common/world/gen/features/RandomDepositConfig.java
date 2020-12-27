package endymion.common.world.gen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class RandomDepositConfig implements IFeatureConfig {

    public final BlockStateProvider stateProvider;
    public final int blobSize;
    public final int blobHeight;
    public final int blobSizeRandom;
    public final int blobHeightRandom;
    public final int blobCount;
    public final int blobCountRandom;

    public RandomDepositConfig(BlockStateProvider stateProvider, int blobSize, int blobHeight, int blobSizeRandom, int blobHeightRandom, int blobCount, int blobCountRandom) {
        this.stateProvider = stateProvider;
        this.blobSize = blobSize;
        this.blobHeight = blobHeight;
        this.blobSizeRandom = blobSizeRandom;
        this.blobHeightRandom = blobHeightRandom;
        this.blobCount = blobCount;
        this.blobCountRandom = blobCountRandom;
    }
}
