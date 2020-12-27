package endymion.common.block;

import endymion.common.world.gen.features.EndFeatures;
import endymion.util.IRegistry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.HugeFungusConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

@ObjectHolder("endymion")
public final class EndBlocks {
    public static final Block SHADELITH = inj();

    public static final Block SHADE_DIRT = inj();
    public static final Block SHADE_GRASS_BLOCK = inj();

    public static final Block SHADE_GRASS = inj();
    public static final Block END_ROOTS = inj();
    public static final Block SHALIMON = inj();
    public static final Block FLOWERED_SHALIMON = inj();
    public static final Block LUMRUD_FUNGUS = inj();

    public static final Block LUMRUD_STEM = inj();
    public static final Block STRIPPED_LUMRUD_STEM = inj();
    public static final Block LUMRUD_HYPHAE = inj();
    public static final Block STRIPPED_LUMRUD_HYPHAE = inj();
    public static final Block SHADE_LIGHT = inj();
    public static final Block LUMRUD_SPORE_BLOCK = inj();

    public static final Block LUMRUD_PLANKS = inj();
    public static final Block LUMRUD_SLAB = inj();
    public static final Block LUMRUD_STAIRS = inj();
    public static final Block LUMRUD_FENCE = inj();

    private EndBlocks() {
    }

    public static void register(IRegistry<Block> registry) {
        registry.register("endymion:shadelith", new Block(stone(1.2, 5, MaterialColor.BLACK).sound(SoundType.BASALT)));

        registry.register("endymion:shade_dirt", new Block(dirt(0.5, MaterialColor.OBSIDIAN)));
        registry.register("endymion:shade_grass_block", new Block(dirt(0.6, MaterialColor.FOLIAGE).sound(SoundType.PLANT)));

        registry.register("endymion:shade_grass", shadeBush(grass(MaterialColor.FOLIAGE).sound(SoundType.CROP)));
        registry.register("endymion:end_roots", shadeBush(roots(MaterialColor.FOLIAGE).sound(SoundType.ROOTS)));
        registry.register("endymion:shalimon", shadeBush(plant(MaterialColor.CLAY).sound(SoundType.FUNGUS)));
        registry.register("endymion:flowered_shalimon", shadeBush(plant(MaterialColor.CLAY).sound(SoundType.FUNGUS)));
        registry.register("endymion:lumrud_fungus", shadeFungus(plant(MaterialColor.FOLIAGE).sound(SoundType.FUNGUS), () -> EndFeatures.LUMRUD_FUNGUS_PLANTED));

        registry.register("endymion:lumrud_stem", stripableLog(wood(MaterialColor.BLACK), () -> STRIPPED_LUMRUD_STEM));
        registry.register("endymion:stripped_lumrud_stem", new RotatedPillarBlock(wood(MaterialColor.BLACK)));
        registry.register("endymion:lumrud_hyphae", stripableLog(wood(MaterialColor.BLACK), () -> STRIPPED_LUMRUD_HYPHAE));
        registry.register("endymion:stripped_lumrud_hyphae", new RotatedPillarBlock(wood(MaterialColor.BLACK)));
        registry.register("endymion:shade_light", new Block(fungilight(1, MaterialColor.LIME)));
        registry.register("endymion:lumrud_spore_block", new Block(spores(MaterialColor.FOLIAGE)));

        registry.register("endymion:lumrud_planks", new Block(wood(MaterialColor.CYAN_TERRACOTTA)));
        registry.register("endymion:lumrud_slab", new SlabBlock(wood(MaterialColor.CYAN_TERRACOTTA)));
        registry.register("endymion:lumrud_stairs", stairs(() -> LUMRUD_PLANKS, wood(MaterialColor.CYAN_TERRACOTTA)));
        registry.register("endymion:lumrud_fence", new FenceBlock(wood(MaterialColor.CYAN_TERRACOTTA)));
    }

    public static void registerItems(IRegistry<Item> registry) {
        registry.register(item(SHADELITH, ItemGroup.BUILDING_BLOCKS));

        registry.register(item(SHADE_DIRT, ItemGroup.BUILDING_BLOCKS));
        registry.register(item(SHADE_GRASS_BLOCK, ItemGroup.BUILDING_BLOCKS));

        registry.register(item(SHADE_GRASS, ItemGroup.DECORATIONS));
        registry.register(item(END_ROOTS, ItemGroup.DECORATIONS));
        registry.register(item(SHALIMON, ItemGroup.DECORATIONS));
        registry.register(item(FLOWERED_SHALIMON, ItemGroup.DECORATIONS));
        registry.register(item(LUMRUD_FUNGUS, ItemGroup.DECORATIONS));

        registry.register(item(LUMRUD_STEM, ItemGroup.BUILDING_BLOCKS));
        registry.register(item(STRIPPED_LUMRUD_STEM, ItemGroup.BUILDING_BLOCKS));
        registry.register(item(LUMRUD_HYPHAE, ItemGroup.BUILDING_BLOCKS));
        registry.register(item(STRIPPED_LUMRUD_HYPHAE, ItemGroup.BUILDING_BLOCKS));
        registry.register(item(SHADE_LIGHT, ItemGroup.DECORATIONS));
        registry.register(item(LUMRUD_SPORE_BLOCK, ItemGroup.BUILDING_BLOCKS));

        registry.register(item(LUMRUD_PLANKS, ItemGroup.BUILDING_BLOCKS));
        registry.register(item(LUMRUD_SLAB, ItemGroup.BUILDING_BLOCKS));
        registry.register(item(LUMRUD_STAIRS, ItemGroup.BUILDING_BLOCKS));
        registry.register(item(LUMRUD_FENCE, ItemGroup.BUILDING_BLOCKS));
    }

    @OnlyIn(Dist.CLIENT)
    public static void setupClient() {
        RenderTypeLookup.setRenderLayer(SHADE_GRASS, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SHALIMON, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(END_ROOTS, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(FLOWERED_SHALIMON, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(LUMRUD_FUNGUS, RenderType.getCutout());
    }

    private static AbstractBlock.Properties spores(MaterialColor color) {
        return AbstractBlock.Properties.create(Material.ORGANIC, color)
                                       .hardnessAndResistance(1)
                                       .sound(SoundType.WART_BLOCK);
    }

    private static AbstractBlock.Properties stone(double strength, double resistance, MaterialColor color) {
        return AbstractBlock.Properties.create(Material.ROCK, color)
                                       .hardnessAndResistance((float) strength, (float) resistance)
                                       .sound(SoundType.STONE)
                                       .harvestTool(ToolType.PICKAXE);
    }

    private static AbstractBlock.Properties wood(MaterialColor color) {
        return AbstractBlock.Properties.create(Material.WOOD, color)
                                       .hardnessAndResistance(2)
                                       .sound(SoundType.WOOD)
                                       .harvestTool(ToolType.AXE);
    }

    private static AbstractBlock.Properties stem(MaterialColor color) {
        return AbstractBlock.Properties.create(Material.NETHER_WOOD, color)
                                       .hardnessAndResistance(2)
                                       .sound(SoundType.NETHER_STEM)
                                       .harvestTool(ToolType.AXE);
    }

    private static AbstractBlock.Properties fungilight(double strength, MaterialColor color) {
        return AbstractBlock.Properties.create(Material.ORGANIC, color)
                                       .hardnessAndResistance((float) strength)
                                       .sound(SoundType.SHROOMLIGHT)
                                       .luminance(state -> 15)
                                       .harvestTool(ToolType.HOE);
    }

    private static AbstractBlock.Properties dirt(double strength, MaterialColor color) {
        return AbstractBlock.Properties.create(Material.EARTH, color)
                                       .hardnessAndResistance((float) strength)
                                       .sound(SoundType.GROUND)
                                       .harvestTool(ToolType.SHOVEL);
    }

    private static AbstractBlock.Properties plant(MaterialColor color) {
        return AbstractBlock.Properties.create(Material.PLANTS, color)
                                       .sound(SoundType.PLANT)
                                       .nonOpaque()
                                       .zeroHardnessAndResistance();
    }

    private static AbstractBlock.Properties roots(MaterialColor color) {
        return AbstractBlock.Properties.create(Material.NETHER_SHOOTS, color)
                                       .sound(SoundType.ROOTS)
                                       .nonOpaque()
                                       .zeroHardnessAndResistance();
    }

    private static AbstractBlock.Properties grass(MaterialColor color) {
        return AbstractBlock.Properties.create(Material.TALL_PLANTS, color)
                                       .sound(SoundType.PLANT)
                                       .nonOpaque()
                                       .zeroHardnessAndResistance();
    }

    private static StairsBlock stairs(Supplier<Block> model, AbstractBlock.Properties properties) {
        return new StairsBlock(() -> model.get().getDefaultState(), properties);
    }

    private static BushBlock shadeBush(AbstractBlock.Properties properties) {
        return new BushBlock(properties) {
            @Override
            protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
                return super.isValidGround(state, world, pos) || state.isIn(EndBlocks.SHADE_DIRT) || state.isIn(EndBlocks.SHADE_GRASS_BLOCK);
            }

            @Override
            public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
                return VoxelShapes.empty();
            }
        };
    }

    private static FungusBlock shadeFungus(AbstractBlock.Properties properties, Supplier<ConfiguredFeature<HugeFungusConfig, ?>> feature) {
        return new FungusBlock(properties, feature) {
            @Override
            protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
                return state.isIn(Blocks.GRASS_BLOCK)
                           || state.isIn(Blocks.DIRT)
                           || state.isIn(Blocks.COARSE_DIRT)
                           || state.isIn(Blocks.PODZOL)
                           || state.isIn(Blocks.FARMLAND)
                           || state.isIn(EndBlocks.SHADE_DIRT)
                           || state.isIn(EndBlocks.SHADE_GRASS_BLOCK);
            }

            @Override
            public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
                return VoxelShapes.empty();
            }
        };
    }

    private static RotatedPillarBlock stripableLog(AbstractBlock.Properties properties, Supplier<Block> stripped) {
        return new RotatedPillarBlock(properties) {
            @Nullable
            @Override
            public BlockState getToolModifiedState(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stack, ToolType toolType) {
                if (toolType == ToolType.AXE) {
                    return stripped.get().getDefaultState().with(AXIS, state.get(AXIS));
                }
                return null;
            }
        };
    }

    private static BlockItem item(Block block, Item.Properties props) {
        ResourceLocation id = block.getRegistryName();
        assert id != null;

        BlockItem item = new BlockItem(block, props);
        item.setRegistryName(id);
        return item;
    }

    private static BlockItem item(Block block, ItemGroup group) {
        return item(block, new Item.Properties().group(group));
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static <T> T inj() {
        return null;
    }
}
