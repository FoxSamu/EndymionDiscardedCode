package endymion.data.models;

import endymion.common.block.EndBlocks;
import endymion.data.models.modelgen.IModelGen;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static endymion.data.models.modelgen.InheritingModelGen.*;

public final class ItemModelTable {
    private static BiConsumer<Item, IModelGen> consumer;

    public static void registerItemModels(BiConsumer<Item, IModelGen> c) {
        consumer = c;

        register(EndBlocks.SHADELITH, item -> inherit(name(item, "block/%s")));
        register(EndBlocks.SHADE_DIRT, item -> inherit(name(item, "block/%s")));
        register(EndBlocks.SHADE_GRASS_BLOCK, item -> inherit(name(item, "block/%s")));
        register(EndBlocks.SHADE_LIGHT, item -> inherit(name(item, "block/%s")));
        register(EndBlocks.LUMRUD_PLANKS, item -> inherit(name(item, "block/%s")));
        register(EndBlocks.LUMRUD_SLAB, item -> inherit(name(item, "block/%s")));
        register(EndBlocks.LUMRUD_STAIRS, item -> inherit(name(item, "block/%s")));
        register(EndBlocks.LUMRUD_FENCE, item -> fenceInventory(name(item, "block/%s_planks", "_fence")));
        register(EndBlocks.LUMRUD_STEM, item -> inherit(name(item, "block/%s")));
        register(EndBlocks.LUMRUD_HYPHAE, item -> inherit(name(item, "block/%s")));
        register(EndBlocks.STRIPPED_LUMRUD_STEM, item -> inherit(name(item, "block/%s")));
        register(EndBlocks.STRIPPED_LUMRUD_HYPHAE, item -> inherit(name(item, "block/%s")));
        register(EndBlocks.LUMRUD_SPORE_BLOCK, item -> inherit(name(item, "block/%s")));

        register(EndBlocks.SHADE_GRASS, item -> generated(name(item, "block/%s")));
        register(EndBlocks.END_ROOTS, item -> generated(name(item, "block/%s")));
        register(EndBlocks.SHALIMON, item -> generated(name(item, "block/%s")));
        register(EndBlocks.FLOWERED_SHALIMON, item -> generated(name(item, "block/%s")));
        register(EndBlocks.LUMRUD_FUNGUS, item -> generated(name(item, "block/%s")));
    }



    private static void register(IItemProvider provider, Function<Item, IModelGen> genFactory) {
        Item item = provider.asItem();
        IModelGen gen = genFactory.apply(item);
        consumer.accept(item, gen);
    }

    private static String name(Item item, String nameFormat) {
        ResourceLocation id = item.getRegistryName();
        assert id != null;

        return String.format("%s:%s", id.getNamespace(), String.format(nameFormat, id.getPath()));
    }

    private static String name(Item item) {
        ResourceLocation id = item.getRegistryName();
        assert id != null;
        return id.toString();
    }

    private static String name(Item item, String nameFormat, String omitSuffix) {
        ResourceLocation id = item.getRegistryName();
        assert id != null;

        String path = id.getPath();
        if (path.endsWith(omitSuffix)) {
            path = path.substring(0, path.length() - omitSuffix.length());
        }

        return String.format("%s:%s", id.getNamespace(), String.format(nameFormat, path));
    }

    private ItemModelTable() {
    }
}
