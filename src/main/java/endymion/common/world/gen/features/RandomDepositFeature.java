package endymion.common.world.gen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.Feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomDepositFeature extends Feature<RandomDepositConfig> {
    public static final Codec<RandomDepositConfig> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("state_provider")
                                         .forGetter(config -> config.stateProvider),
            Codec.INT.fieldOf("blob_count")
                     .orElse(3)
                     .forGetter(config -> config.blobCount),
            Codec.INT.fieldOf("blob_count_random")
                     .orElse(2)
                     .forGetter(config -> config.blobCountRandom),
            Codec.INT.fieldOf("blob_height")
                     .orElse(3)
                     .forGetter(config -> config.blobHeight),
            Codec.INT.fieldOf("blob_height_random")
                     .orElse(3)
                     .forGetter(config -> config.blobHeightRandom),
            Codec.INT.fieldOf("blob_size")
                     .orElse(1)
                     .forGetter(config -> config.blobSize),
            Codec.INT.fieldOf("blob_size_random")
                     .orElse(3)
                     .forGetter(config -> config.blobSizeRandom)
        ).apply(
            instance,
            (state, blobCount, blobCountRandom, blobHeight, blobHeightRandom, blobSize, blobSizeRandom) ->
                new RandomDepositConfig(
                    state,
                    blobSize, blobHeight, blobSizeRandom,
                    blobHeightRandom, blobCount, blobCountRandom
                )
        )
    );

    public RandomDepositFeature(Codec<RandomDepositConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader level, ChunkGenerator gen, Random rand, BlockPos pos, RandomDepositConfig config) {
        BlockPos.Mutable mpos = new BlockPos.Mutable();
        mpos.setPos(pos);
        mpos.setY(rand.nextInt(level.getHeight() - 2));
        while (level.getBlockState(mpos).isSolid() && mpos.getY() > 0) {
            mpos.move(Direction.DOWN, 1);
        }
        while (!level.getBlockState(mpos).isSolid() && mpos.getY() > 0) {
            mpos.move(Direction.DOWN, 1);
        }

        mpos.move(Direction.UP);
        pos = mpos.toImmutable();

        Map<BlockPos, Integer> columns = new HashMap<>();

        int c = config.blobCount + rand.nextInt(config.blobCountRandom);
        int sr = config.blobSize / 2 + 5;
        for (int i = 0; i < c; i++) {
            BlockPos off = pos.add(rand.nextInt(sr) - rand.nextInt(sr), 0, rand.nextInt(sr) - rand.nextInt(sr));
            double radius = config.blobSize + rand.nextDouble() * config.blobSizeRandom + 0.2;
            genBlob(level, off, radius, config.blobHeight + rand.nextInt(config.blobHeightRandom), rand, columns);
        }

        for (Map.Entry<BlockPos, Integer> column : columns.entrySet()) {
            BlockPos cpos = column.getKey();
            Integer h = column.getValue();
            mpos.setPos(cpos);
            for (int y = 0; y < h; y++) {
                mpos.setPos(cpos).move(0, y, 0);

                BlockState s = level.getBlockState(mpos);
                if (!s.isSolid()) {
                    level.setBlockState(mpos, config.stateProvider.getBlockState(rand, mpos), 2);
                }
            }
        }
        return true;
    }

    private void genBlob(ISeedReader level, BlockPos pos, double radius, int height, Random rand, Map<BlockPos, Integer> columns) {
        BlockPos.Mutable mpos = new BlockPos.Mutable();

        int rad = (int) (radius + 1);
        for (int x = -rad; x <= rad; x++) {
            for (int z = -rad; z <= rad; z++) {
                double ri = radius - 1.5;
                if (x * x + z * z < radius * radius) {
                    int h = height + rand.nextInt(2);
                    if (x * x + z * z >= ri * ri) {
                        h = h * 2 / 3;
                    }

                    int startY = Integer.MAX_VALUE;
                    boolean air = false;

                    for (int y = height + 3; y > -10; y--) {
                        mpos.setPos(pos).move(x, y, z);
                        if (mpos.getY() < 0) {
                            break;
                        }

                        BlockState s = level.getBlockState(mpos);
                        if (!s.isSolid()) {
                            air = true;
                        }
                        if (s.isSolid() && air) {
                            startY = y + 1;
                            break;
                        }
                    }

                    if (startY != Integer.MAX_VALUE) {
                        mpos.setPos(pos).move(x, startY, z);
                        columns.put(mpos.toImmutable(), Math.max(columns.getOrDefault(mpos.toImmutable(), -1), h));
                    }
                }
            }
        }
    }
}
