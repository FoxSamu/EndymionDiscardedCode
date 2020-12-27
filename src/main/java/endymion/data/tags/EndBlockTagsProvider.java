package endymion.data.tags;

import endymion.common.block.EndBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;

import java.nio.file.Path;

public class EndBlockTagsProvider extends TagsProvider<Block> {
    @SuppressWarnings("deprecation") // We need Registry.BLOCK. Sorry Forge...
    public EndBlockTagsProvider(DataGenerator gen) {
        super(gen, Registry.BLOCK);
    }

    @Override
    protected void registerTags() {
        getOrCreateTagBuilder(Tags.Blocks.DIRT).replace(false).add(
            EndBlocks.SHADE_DIRT,
            EndBlocks.SHADE_GRASS_BLOCK
        );

        getOrCreateTagBuilder(BlockTags.ENDERMAN_HOLDABLE).replace(false).add(
            EndBlocks.SHADE_DIRT,
            EndBlocks.SHADE_GRASS_BLOCK,
            EndBlocks.SHALIMON,
            EndBlocks.FLOWERED_SHALIMON,
            EndBlocks.END_ROOTS
        );

        getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).replace(false).add(
            EndBlocks.LUMRUD_SLAB
        );

        getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).replace(false).add(
            EndBlocks.LUMRUD_STAIRS
        );

        getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).replace(false).add(
            EndBlocks.LUMRUD_FENCE
        );
    }

    protected ITag.Builder getBuilder(ITag.INamedTag<Block> namedTag) {
        return tagToBuilder.computeIfAbsent(namedTag.getId(), id -> new ITag.Builder());
    }

    @Override
    protected Path makePath(ResourceLocation id) {
        return generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Endymion/BlockTags";
    }
}
