package endymion.common.world.gen.placer;

import endymion.EndymionMod;
import endymion.util.IRegistry;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;

import java.util.ArrayList;
import java.util.List;

public final class EndBlockPlacerTypes {
    private static final List<BlockPlacerType<?>> TYPES = new ArrayList<>();

    public static final BlockPlacerType<SelectiveBlockPlacer> SELECTIVE = register("selective", new BlockPlacerType<>(SelectiveBlockPlacer.CODEC));
    public static final BlockPlacerType<OffsetBlockPlacer> OFFSET = register("offset", new BlockPlacerType<>(OffsetBlockPlacer.CODEC));

    private EndBlockPlacerTypes() {
    }

    private static <T extends BlockPlacerType<?>> T register(String id, T t) {
        TYPES.add(t);
        t.setRegistryName(EndymionMod.resLoc(id));
        return t;
    }

    public static void register(IRegistry<BlockPlacerType<?>> registry) {
        TYPES.forEach(registry::register);
    }
}
