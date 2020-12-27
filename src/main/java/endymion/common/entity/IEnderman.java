package endymion.common.entity;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.EndermanEntity;

@FunctionalInterface
public interface IEnderman {
    boolean randomTeleport();
    default EndermanEntity ender() {
        return (EndermanEntity) this;
    }
}
