package endymion.data.models;

import endymion.common.block.EndBlocks;
import endymion.data.models.modelgen.IModelGen;
import endymion.data.models.stategen.*;
import net.minecraft.block.Block;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static endymion.data.models.modelgen.InheritingModelGen.*;

public final class BlockStateTable {
    private static BiConsumer<Block, IBlockStateGen> consumer;

    public static void registerBlockStates(BiConsumer<Block, IBlockStateGen> c) {
        consumer = c;

        register(EndBlocks.SHADELITH, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));

        register(EndBlocks.SHADE_DIRT, block -> rotateXY(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(EndBlocks.SHADE_LIGHT, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(EndBlocks.SHADE_GRASS_BLOCK, block -> rotateY(name(block, "block/%s"), cubeBottomTop(name(block, "block/shade_dirt"), name(block, "block/%s_top"), name(block, "block/%s_side"))));

        register(EndBlocks.SHADE_GRASS, block -> simple(name(block, "block/%s"), cross(name(block, "block/%s"))));
        register(EndBlocks.END_ROOTS, block -> simple(name(block, "block/%s"), cross(name(block, "block/%s"))));
        register(EndBlocks.SHALIMON, block -> simple(name(block, "block/%s"), cross(name(block, "block/%s"))));
        register(EndBlocks.FLOWERED_SHALIMON, block -> simple(name(block, "block/%s"), cross(name(block, "block/%s"))));
        register(EndBlocks.LUMRUD_FUNGUS, block -> simple(name(block, "block/%s"), cross(name(block, "block/%s"))));

        register(EndBlocks.LUMRUD_PLANKS, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(EndBlocks.LUMRUD_SLAB, block -> simple(name(block, "block/%s"), slab(name(block, "block/%s_planks", "_slab"))));
        register(EndBlocks.LUMRUD_STAIRS, block -> simple(name(block, "block/%s"), stairs(name(block, "block/%s_planks", "_stairs"))));
        register(EndBlocks.LUMRUD_FENCE, block -> fence(name(block, "block/%s"), name(block, "block/%s_planks", "_fence")));

        register(EndBlocks.LUMRUD_SPORE_BLOCK, block -> simple(name(block, "block/%s"), cubeAll(name(block, "block/%s"))));
        register(EndBlocks.LUMRUD_STEM, block -> rotatedPillar(name(block, "block/%s"), cubeColumn(name(block, "block/%s_top"), name(block, "block/%s"))));
        register(EndBlocks.STRIPPED_LUMRUD_STEM, block -> rotatedPillar(name(block, "block/%s"), cubeColumn(name(block, "block/%s_top"), name(block, "block/%s"))));
        register(EndBlocks.LUMRUD_HYPHAE, block -> rotatedPillar(name(block, "block/%s"), cubeAll(name(block, "block/%s_stem", "_hyphae"))));
        register(EndBlocks.STRIPPED_LUMRUD_HYPHAE, block -> rotatedPillar(name(block, "block/%s"), cubeAll(name(block, "block/%s_stem", "_hyphae"))));
    }

    private static IBlockStateGen simple(String name, IModelGen model) {
        return VariantsBlockStateGen.variants(ModelInfo.create(name, model));
    }

    private static IBlockStateGen fence(String name, String texName) {
        return MultipartBlockStateGen.multipart()
                                     .part(ModelInfo.create(name + "_post", fencePost(texName)))
                                     .part(
                                         Selector.and().condition("north", "true"),
                                         ModelInfo.create(name + "_side", fenceSide(texName))
                                     )
                                     .part(
                                         Selector.and().condition("east", "true"),
                                         ModelInfo.create(name + "_side", fenceSide(texName))
                                                  .rotate(0, 90)
                                                  .uvlock(true)
                                     )
                                     .part(
                                         Selector.and().condition("south", "true"),
                                         ModelInfo.create(name + "_side", fenceSide(texName))
                                                  .rotate(0, 180)
                                                  .uvlock(true)
                                     )
                                     .part(
                                         Selector.and().condition("west", "true"),
                                         ModelInfo.create(name + "_side", fenceSide(texName))
                                                  .rotate(0, 270)
                                                  .uvlock(true)
                                     );
    }

    private static IBlockStateGen wallRandomized(String name, int... weights) {
        ModelInfo[] post = new ModelInfo[weights.length];
        ModelInfo[] sideN = new ModelInfo[weights.length];
        ModelInfo[] sideE = new ModelInfo[weights.length];
        ModelInfo[] sideS = new ModelInfo[weights.length];
        ModelInfo[] sideW = new ModelInfo[weights.length];

        for (int i = 0; i < weights.length; i++) {
            String n = name + (i == 0 ? "_wall" : "_wall_alt_" + i);
            String tn = name + (i == 0 ? "" : "_alt_" + i);

            int w = weights[i];
            post[i] = ModelInfo.create(n + "_post", wallPost(tn)).weight(w);
            sideN[i] = ModelInfo.create(n + "_side", wallSide(tn)).uvlock(true).rotate(0, 0).weight(w);
            sideE[i] = ModelInfo.create(n + "_side", wallSide(tn)).uvlock(true).rotate(0, 90).weight(w);
            sideS[i] = ModelInfo.create(n + "_side", wallSide(tn)).uvlock(true).rotate(0, 180).weight(w);
            sideW[i] = ModelInfo.create(n + "_side", wallSide(tn)).uvlock(true).rotate(0, 270).weight(w);
        }

        return MultipartBlockStateGen.multipart()
                                     .part(Selector.and().condition("up", "true"), post)
                                     .part(Selector.and().condition("north", "true"), sideN)
                                     .part(Selector.and().condition("east", "true"), sideE)
                                     .part(Selector.and().condition("south", "true"), sideS)
                                     .part(Selector.and().condition("west", "true"), sideW);
    }

    private static IBlockStateGen cubeAllRandomized(String name, int... weights) {
        ModelInfo[] random = new ModelInfo[weights.length];
        for (int i = 0; i < random.length; i++) {
            String n = name + (i == 0 ? "" : "_alt_" + i);
            random[i] = ModelInfo.create(n, cubeAll(n)).weight(weights[i]);
        }
        return VariantsBlockStateGen.variants(random);
    }

    private static IBlockStateGen slabRandomized(String name, int... weights) {
        ModelInfo[] lo = new ModelInfo[weights.length];
        ModelInfo[] hi = new ModelInfo[weights.length];
        ModelInfo[] dbl = new ModelInfo[weights.length];
        for (int i = 0; i < lo.length; i++) {
            String ln = name + (i == 0 ? "_slab" : "_slab_alt_" + i);
            String hn = name + (i == 0 ? "_slab_top" : "_slab_top_alt_" + i);
            String n = name + (i == 0 ? "" : "_alt_" + i);
            lo[i] = ModelInfo.create(ln, slab(n)).weight(weights[i]);
            hi[i] = ModelInfo.create(hn, slabTop(n)).weight(weights[i]);
            dbl[i] = ModelInfo.create(n).weight(weights[i]);
        }
        return VariantsBlockStateGen.variants("type=bottom", lo)
                                    .variant("type=top", hi)
                                    .variant("type=double", dbl);
    }

    private static IBlockStateGen stairsRandomized(String name, int... weights) {
        ModelInfo[] innerL = new ModelInfo[weights.length];
        ModelInfo[] outerL = new ModelInfo[weights.length];
        ModelInfo[] innerR = new ModelInfo[weights.length];
        ModelInfo[] outerR = new ModelInfo[weights.length];
        ModelInfo[] stairs = new ModelInfo[weights.length];
        boolean[] innerM = new boolean[weights.length];
        boolean[] outerM = new boolean[weights.length];
        boolean[] stairsM = new boolean[weights.length];

        VariantsBlockStateGen gen = VariantsBlockStateGen.variants();
        int y = 270;
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            for (Half half : Half.values()) {
                int x = half == Half.TOP ? 180 : 0;
                String state = String.format("facing=%s,half=%s", dir.getString(), half.getString());

                for (int i = 0; i < innerL.length; i++) {
                    String in = name + (i == 0 ? "_stairs_inner" : "_stairs_inner_alt_" + i);
                    String on = name + (i == 0 ? "_stairs_outer" : "_stairs_outer_alt_" + i);
                    String sn = name + (i == 0 ? "_stairs" : "_stairs_alt_" + i);
                    String tn = name + (i == 0 ? "" : "_alt_" + i);

                    int yp = y == 0 ? 270 : y - 90;
                    int yn = y == 270 ? 0 : y + 90;

                    innerL[i] = ModelInfo.create(in, innerM[i] ? null : stairsInner(tn))
                                         .rotate(x, x == 180 ? y : yp)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    outerL[i] = ModelInfo.create(on, outerM[i] ? null : stairsOuter(tn))
                                         .rotate(x, x == 180 ? y : yp)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    innerR[i] = ModelInfo.create(in)
                                         .rotate(x, x == 180 ? yn : y)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    outerR[i] = ModelInfo.create(on)
                                         .rotate(x, x == 180 ? yn : y)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    stairs[i] = ModelInfo.create(sn, stairsM[i] ? null : stairs(tn))
                                         .rotate(x, y)
                                         .uvlock(true)
                                         .weight(weights[i]);

                    innerM[i] = true;
                    outerM[i] = true;
                    stairsM[i] = true;
                }

                gen.variant(state + ",shape=straight", stairs);
                gen.variant(state + ",shape=inner_left", innerL);
                gen.variant(state + ",shape=inner_right", innerR);
                gen.variant(state + ",shape=outer_left", outerL);
                gen.variant(state + ",shape=outer_right", outerR);
            }
            if (y == 270) y = 0;
            else y += 90;
        }
        return gen;
    }

//    private static IBlockStateGen stepRandomized(String name, int... weights) {
//        ModelInfo[] innerL = new ModelInfo[weights.length];
//        ModelInfo[] outerL = new ModelInfo[weights.length];
//        ModelInfo[] innerR = new ModelInfo[weights.length];
//        ModelInfo[] outerR = new ModelInfo[weights.length];
//        ModelInfo[] stairs = new ModelInfo[weights.length];
//        boolean[] innerM = new boolean[weights.length];
//        boolean[] outerM = new boolean[weights.length];
//        boolean[] stairsM = new boolean[weights.length];
//
//        VariantsBlockStateGen gen = VariantsBlockStateGen.variants();
//        int y = 270;
//        for (Direction dir : Direction.Plane.HORIZONTAL) {
//            for (Half half : Half.values()) {
//                int x = half == Half.TOP ? 180 : 0;
//                String state = String.format("facing=%s,half=%s", dir.getString(), half.getString());
//
//                for (int i = 0; i < innerL.length; i++) {
//                    String in = name + (i == 0 ? "_step_inner" : "_step_inner_alt_" + i);
//                    String on = name + (i == 0 ? "_step_outer" : "_step_outer_alt_" + i);
//                    String sn = name + (i == 0 ? "_step" : "_step_alt_" + i);
//                    String tn = name + (i == 0 ? "" : "_alt_" + i);
//
//                    int yp = y == 0 ? 270 : y - 90;
//                    int yn = y == 270 ? 0 : y + 90;
//
//                    innerL[i] = ModelInfo.create(in, innerM[i] ? null : stepInner(tn))
//                                         .rotate(x, x == 180 ? y : yp)
//                                         .uvlock(true)
//                                         .weight(weights[i]);
//                    outerL[i] = ModelInfo.create(on, outerM[i] ? null : stepOuter(tn))
//                                         .rotate(x, x == 180 ? y : yp)
//                                         .uvlock(true)
//                                         .weight(weights[i]);
//                    innerR[i] = ModelInfo.create(in)
//                                         .rotate(x, x == 180 ? yn : y)
//                                         .uvlock(true)
//                                         .weight(weights[i]);
//                    outerR[i] = ModelInfo.create(on)
//                                         .rotate(x, x == 180 ? yn : y)
//                                         .uvlock(true)
//                                         .weight(weights[i]);
//                    stairs[i] = ModelInfo.create(sn, stairsM[i] ? null : step(tn))
//                                         .rotate(x, y)
//                                         .uvlock(true)
//                                         .weight(weights[i]);
//
//                    innerM[i] = true;
//                    outerM[i] = true;
//                    stairsM[i] = true;
//                }
//
//                gen.variant(state + ",shape=straight", stairs);
//                gen.variant(state + ",shape=inner_left", innerL);
//                gen.variant(state + ",shape=inner_right", innerR);
//                gen.variant(state + ",shape=outer_left", outerL);
//                gen.variant(state + ",shape=outer_right", outerR);
//            }
//            if (y == 270) y = 0;
//            else y += 90;
//        }
//        return gen;
//    }

    private static IBlockStateGen woodenSlab(String modelName, String name, int... weights) {
        ModelInfo[] lo = new ModelInfo[weights.length];
        ModelInfo[] hi = new ModelInfo[weights.length];
        ModelInfo[] dbl = new ModelInfo[weights.length];
        for (int i = 0; i < lo.length; i++) {
            String ln = modelName + (i == 0 ? "_slab" : "_slab_alt_" + i);
            String hn = modelName + (i == 0 ? "_slab_top" : "_slab_top_alt_" + i);
            String n = name + (i == 0 ? "" : "_alt_" + i);
            lo[i] = ModelInfo.create(ln, slab(n)).weight(weights[i]);
            hi[i] = ModelInfo.create(hn, slabTop(n)).weight(weights[i]);
            dbl[i] = ModelInfo.create(n).weight(weights[i]);
        }
        return VariantsBlockStateGen.variants("type=bottom", lo)
                                    .variant("type=top", hi)
                                    .variant("type=double", dbl);
    }

    private static IBlockStateGen woodenStairs(String modelName, String name, int... weights) {
        ModelInfo[] innerL = new ModelInfo[weights.length];
        ModelInfo[] outerL = new ModelInfo[weights.length];
        ModelInfo[] innerR = new ModelInfo[weights.length];
        ModelInfo[] outerR = new ModelInfo[weights.length];
        ModelInfo[] stairs = new ModelInfo[weights.length];
        boolean[] innerM = new boolean[weights.length];
        boolean[] outerM = new boolean[weights.length];
        boolean[] stairsM = new boolean[weights.length];

        VariantsBlockStateGen gen = VariantsBlockStateGen.variants();
        int y = 270;
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            for (Half half : Half.values()) {
                int x = half == Half.TOP ? 180 : 0;
                String state = String.format("facing=%s,half=%s", dir.getString(), half.getString());

                for (int i = 0; i < innerL.length; i++) {
                    String in = modelName + (i == 0 ? "_stairs_inner" : "_stairs_inner_alt_" + i);
                    String on = modelName + (i == 0 ? "_stairs_outer" : "_stairs_outer_alt_" + i);
                    String sn = modelName + (i == 0 ? "_stairs" : "_stairs_alt_" + i);
                    String tn = name + (i == 0 ? "" : "_alt_" + i);

                    int yp = y == 0 ? 270 : y - 90;
                    int yn = y == 270 ? 0 : y + 90;

                    innerL[i] = ModelInfo.create(in, innerM[i] ? null : stairsInner(tn))
                                         .rotate(x, x == 180 ? y : yp)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    outerL[i] = ModelInfo.create(on, outerM[i] ? null : stairsOuter(tn))
                                         .rotate(x, x == 180 ? y : yp)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    innerR[i] = ModelInfo.create(in)
                                         .rotate(x, x == 180 ? yn : y)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    outerR[i] = ModelInfo.create(on)
                                         .rotate(x, x == 180 ? yn : y)
                                         .uvlock(true)
                                         .weight(weights[i]);
                    stairs[i] = ModelInfo.create(sn, stairsM[i] ? null : stairs(tn))
                                         .rotate(x, y)
                                         .uvlock(true)
                                         .weight(weights[i]);

                    innerM[i] = true;
                    outerM[i] = true;
                    stairsM[i] = true;
                }

                gen.variant(state + ",shape=straight", stairs);
                gen.variant(state + ",shape=inner_left", innerL);
                gen.variant(state + ",shape=inner_right", innerR);
                gen.variant(state + ",shape=outer_left", outerL);
                gen.variant(state + ",shape=outer_right", outerR);
            }
            if (y == 270) y = 0;
            else y += 90;
        }
        return gen;
    }

//    private static IBlockStateGen woodenStep(String modelName, String name, int... weights) {
//        ModelInfo[] innerL = new ModelInfo[weights.length];
//        ModelInfo[] outerL = new ModelInfo[weights.length];
//        ModelInfo[] innerR = new ModelInfo[weights.length];
//        ModelInfo[] outerR = new ModelInfo[weights.length];
//        ModelInfo[] stairs = new ModelInfo[weights.length];
//        boolean[] innerM = new boolean[weights.length];
//        boolean[] outerM = new boolean[weights.length];
//        boolean[] stairsM = new boolean[weights.length];
//
//        VariantsBlockStateGen gen = VariantsBlockStateGen.variants();
//        int y = 270;
//        for (Direction dir : Direction.Plane.HORIZONTAL) {
//            for (Half half : Half.values()) {
//                int x = half == Half.TOP ? 180 : 0;
//                String state = String.format("facing=%s,half=%s", dir.getString(), half.getString());
//
//                for (int i = 0; i < innerL.length; i++) {
//                    String in = modelName + (i == 0 ? "_step_inner" : "_step_inner_alt_" + i);
//                    String on = modelName + (i == 0 ? "_step_outer" : "_step_outer_alt_" + i);
//                    String sn = modelName + (i == 0 ? "_step" : "_step_alt_" + i);
//                    String tn = name + (i == 0 ? "" : "_alt_" + i);
//
//                    int yp = y == 0 ? 270 : y - 90;
//                    int yn = y == 270 ? 0 : y + 90;
//
//                    innerL[i] = ModelInfo.create(in, innerM[i] ? null : stepInner(tn))
//                                         .rotate(x, x == 180 ? y : yp)
//                                         .uvlock(true)
//                                         .weight(weights[i]);
//                    outerL[i] = ModelInfo.create(on, outerM[i] ? null : stepOuter(tn))
//                                         .rotate(x, x == 180 ? y : yp)
//                                         .uvlock(true)
//                                         .weight(weights[i]);
//                    innerR[i] = ModelInfo.create(in)
//                                         .rotate(x, x == 180 ? yn : y)
//                                         .uvlock(true)
//                                         .weight(weights[i]);
//                    outerR[i] = ModelInfo.create(on)
//                                         .rotate(x, x == 180 ? yn : y)
//                                         .uvlock(true)
//                                         .weight(weights[i]);
//                    stairs[i] = ModelInfo.create(sn, stairsM[i] ? null : step(tn))
//                                         .rotate(x, y)
//                                         .uvlock(true)
//                                         .weight(weights[i]);
//
//                    innerM[i] = true;
//                    outerM[i] = true;
//                    stairsM[i] = true;
//                }
//
//                gen.variant(state + ",shape=straight", stairs);
//                gen.variant(state + ",shape=inner_left", innerL);
//                gen.variant(state + ",shape=inner_right", innerR);
//                gen.variant(state + ",shape=outer_left", outerL);
//                gen.variant(state + ",shape=outer_right", outerR);
//            }
//            if (y == 270) y = 0;
//            else y += 90;
//        }
//        return gen;
//    }

    private static IBlockStateGen rotateY(String name, IModelGen model) {
        return VariantsBlockStateGen.variants(
            ModelInfo.create(name, model).rotate(0, 0),
            ModelInfo.create(name, model).rotate(0, 90),
            ModelInfo.create(name, model).rotate(0, 180),
            ModelInfo.create(name, model).rotate(0, 270)
        );
    }

    private static IBlockStateGen rotateXY(String name, IModelGen model) {
        return VariantsBlockStateGen.variants(
            ModelInfo.create(name, model).rotate(0, 0),
            ModelInfo.create(name, model).rotate(0, 90),
            ModelInfo.create(name, model).rotate(0, 180),
            ModelInfo.create(name, model).rotate(0, 270),
            ModelInfo.create(name, model).rotate(90, 0),
            ModelInfo.create(name, model).rotate(90, 90),
            ModelInfo.create(name, model).rotate(90, 180),
            ModelInfo.create(name, model).rotate(90, 270),
            ModelInfo.create(name, model).rotate(180, 0),
            ModelInfo.create(name, model).rotate(180, 90),
            ModelInfo.create(name, model).rotate(180, 180),
            ModelInfo.create(name, model).rotate(180, 270),
            ModelInfo.create(name, model).rotate(270, 0),
            ModelInfo.create(name, model).rotate(270, 90),
            ModelInfo.create(name, model).rotate(270, 180),
            ModelInfo.create(name, model).rotate(270, 270)
        );
    }

    private static IBlockStateGen doublePlant(String lower, IModelGen lowerModel, String upper, IModelGen upperModel) {
        return VariantsBlockStateGen.variants("half=lower", ModelInfo.create(lower, lowerModel))
                                    .variant("half=upper", ModelInfo.create(upper, upperModel));
    }

    private static IBlockStateGen rotatedPillar(String name, IModelGen model) {
        return VariantsBlockStateGen.variants("axis=y", ModelInfo.create(name, model).rotate(0, 0))
                                    .variant("axis=z", ModelInfo.create(name, model).rotate(90, 0))
                                    .variant("axis=x", ModelInfo.create(name, model).rotate(90, 90));
    }

    private static void register(Block block, Function<Block, IBlockStateGen> genFactory) {
        consumer.accept(block, genFactory.apply(block));
    }

    private static String name(Block block, String nameFormat) {
        ResourceLocation id = block.getRegistryName();
        assert id != null;

        return String.format("%s:%s", id.getNamespace(), String.format(nameFormat, id.getPath()));
    }

    private static String name(Block block, String nameFormat, String omitSuffix) {
        ResourceLocation id = block.getRegistryName();
        assert id != null;

        String path = id.getPath();
        if (path.endsWith(omitSuffix)) {
            path = path.substring(0, path.length() - omitSuffix.length());
        }

        return String.format("%s:%s", id.getNamespace(), String.format(nameFormat, path));
    }

    private static String name(Block block) {
        ResourceLocation id = block.getRegistryName();
        assert id != null;
        return id.toString();
    }


    private BlockStateTable() {
    }
}
