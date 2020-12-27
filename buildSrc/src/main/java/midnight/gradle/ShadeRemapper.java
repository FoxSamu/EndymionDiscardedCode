package midnight.gradle;

import org.objectweb.asm.commons.Remapper;

import java.util.HashMap;
import java.util.Map;

public class ShadeRemapper extends Remapper {
    private final HashMap<String, String> packageRenames = new HashMap<>();

    public void addPackageRename(String oldName, String newName) {
        packageRenames.put(oldName, newName);
    }

    @Override
    public String map(String internalName) {
        for (Map.Entry<String, String> entry : packageRenames.entrySet()) {
            if (internalName.startsWith(entry.getKey())) {
                String sub = internalName.substring(entry.getKey().length());
                if (sub.startsWith("/")) {
                    return entry.getValue() + sub;
                }
            }
        }
        return internalName;
    }
}
