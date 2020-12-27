package endymion.data.models.stategen;

import com.google.gson.JsonElement;
import endymion.data.models.modelgen.IModelGen;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

public interface IBlockStateGen {
    JsonElement makeJson(ResourceLocation id, Block block);
    void getModels(BiConsumer<String, IModelGen> consumer);
}
