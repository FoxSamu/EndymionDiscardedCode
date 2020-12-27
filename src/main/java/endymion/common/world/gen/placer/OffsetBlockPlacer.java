package endymion.common.world.gen.placer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;

import java.util.Random;

public class OffsetBlockPlacer extends BlockPlacer {
    public static final Codec<OffsetBlockPlacer> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            BlockPlacer.TYPE_CODEC
                .fieldOf("placer")
                .forGetter(placer -> placer.placer),
            Codec.INT.fieldOf("x")
                     .orElse(0)
                     .forGetter(placer -> placer.offX),
            Codec.INT.fieldOf("y")
                     .orElse(0)
                     .forGetter(placer -> placer.offY),
            Codec.INT.fieldOf("z")
                     .orElse(0)
                     .forGetter(placer -> placer.offZ)
        ).apply(instance, OffsetBlockPlacer::new)
    );

    private final BlockPlacer placer;
    private final int offX;
    private final int offY;
    private final int offZ;

    public OffsetBlockPlacer(BlockPlacer placer, int offX, int offY, int offZ) {
        this.placer = placer;
        this.offX = offX;
        this.offY = offY;
        this.offZ = offZ;
    }

    @Override
    public void generate(IWorld world, BlockPos pos, BlockState state, Random random) {
        placer.generate(world, pos.add(offX, offY, offZ), state, random);
    }

    @Override
    protected BlockPlacerType<?> getType() {
        return EndBlockPlacerTypes.OFFSET;
    }
}
