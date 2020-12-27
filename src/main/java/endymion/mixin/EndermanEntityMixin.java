package endymion.mixin;

import endymion.common.entity.IEnderman;
import endymion.common.entity.goal.AvoidVoidGoal;
import endymion.common.entity.goal.ChanceWaterAvoidingRandomWalkingGoal;
import endymion.common.entity.goal.LookDownGoal;
import endymion.common.entity.goal.WatchBlockGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends MonsterEntity implements IEnderman {
    protected EndermanEntityMixin(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "registerGoals", at = @At("RETURN"))
    private void onRegisterGoals(CallbackInfo info) {
        goalSelector.addGoal(1, new AvoidVoidGoal(this));
        goalSelector.addGoal(8, new LookAtGoal(this, EndermanEntity.class, 8));
        goalSelector.addGoal(8, new LookDownGoal(this, 0.002));
        goalSelector.addGoal(8, new LookAtGoal(this, CatEntity.class, 8));
        goalSelector.addGoal(8, new WatchBlockGoal(this, state -> state.isIn(BlockTags.ENDERMAN_HOLDABLE), 8, 0.01F));
        goalSelector.addGoal(7, new ChanceWaterAvoidingRandomWalkingGoal(this, 0.7, 120, 0));
    }

    @Shadow
    protected abstract boolean teleportRandomly();

    @Override
    public boolean randomTeleport() {
        return teleportRandomly();
    }
}
