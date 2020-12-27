package endymion.common.entity.goal;

import endymion.common.entity.IEnderman;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class LookDownGoal extends Goal {
    protected final EndermanEntity ender;
    protected final double chance;
    protected int lookTime;

    public LookDownGoal(IEnderman ender, double chance) {
        this.ender = ender.ender();
        this.chance = chance;
        setMutexFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        return ender.getRNG().nextDouble() < chance;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return lookTime > 0 && ender.getAttackTarget() == null;
    }

    @Override
    public void startExecuting() {
        lookTime = 40 + ender.getRNG().nextInt(40);
    }

    @Override
    public void resetTask() {
    }

    @Override
    public void tick() {
        Vector3d vec = ender.getLookVec();
        ender.getLookController().setLookPosition(ender.getX() + vec.getX(), ender.getY(), ender.getZ() + vec.getZ());
        lookTime --;
    }
}
