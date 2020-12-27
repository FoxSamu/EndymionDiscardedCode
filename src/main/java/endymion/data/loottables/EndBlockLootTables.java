package endymion.data.loottables;

import endymion.common.block.EndBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EndBlockLootTables extends BlockLootTables {

    protected static LootTable.Builder droppingNothing() {
        return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(0)));
    }

    @Override
    protected void addTables() {
        registerDropSelfLootTable(EndBlocks.SHADELITH);
        registerDropSelfLootTable(EndBlocks.SHADE_DIRT);
        registerDropSelfLootTable(EndBlocks.SHALIMON);
        registerDropSelfLootTable(EndBlocks.FLOWERED_SHALIMON);
        registerDropSelfLootTable(EndBlocks.END_ROOTS);
        registerDropSelfLootTable(EndBlocks.SHADE_LIGHT);
        registerDropSelfLootTable(EndBlocks.LUMRUD_PLANKS);
        registerDropSelfLootTable(EndBlocks.LUMRUD_STAIRS);
        registerDropSelfLootTable(EndBlocks.LUMRUD_FENCE);
        registerDropSelfLootTable(EndBlocks.LUMRUD_STEM);
        registerDropSelfLootTable(EndBlocks.LUMRUD_HYPHAE);
        registerDropSelfLootTable(EndBlocks.STRIPPED_LUMRUD_STEM);
        registerDropSelfLootTable(EndBlocks.STRIPPED_LUMRUD_HYPHAE);
        registerDropSelfLootTable(EndBlocks.LUMRUD_FUNGUS);
        registerDropSelfLootTable(EndBlocks.LUMRUD_SPORE_BLOCK);
        registerLootTable(EndBlocks.SHADE_GRASS_BLOCK, block -> droppingWithSilkTouch(block, EndBlocks.SHADE_DIRT));
        registerLootTable(EndBlocks.SHADE_GRASS, BlockLootTables::onlyWithShears);
        registerLootTable(EndBlocks.LUMRUD_SLAB, BlockLootTables::droppingSlab);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return StreamSupport.stream(ForgeRegistries.BLOCKS.spliterator(), false)
                            .filter(block -> Objects.requireNonNull(block.getRegistryName()).getNamespace().equals("endymion"))
                            .collect(Collectors.toList());
    }
}
