package endymion.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import endymion.common.block.EndBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class EndymionBlockProvider implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator gen;

    public EndymionBlockProvider(DataGenerator gen) {
        this.gen = gen;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        JsonObject object = new JsonObject();
        for(Block block : Registry.BLOCK) {
            object.add(block.getRegistryName().toString(), new JsonPrimitive(
                String.format("%s - %s: %s", block.getRegistryName(), block.getTranslationKey(), block.getClass().getName())
            ));
        }
        System.out.println(EndBlocks.SHADE_DIRT);
        IDataProvider.save(GSON, cache, object, getPath(gen.getOutputFolder()));
    }

    private static Path getPath(Path path) {
        return path.resolve("reports/blocks.json");
    }

    @Override
    public String getName() {
        return "Something";
    }
}
