package endymion.common.entity.goal;

import endymion.common.entity.IEnderman;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class AvoidVoidGoal extends Goal {
    protected final EndermanEntity ender;

    public AvoidVoidGoal(IEnderman ender) {
        this.ender = ender.ender();
    }

    @Override
    public boolean shouldExecute() {
        if (ender.fallDistance > 3)
            return true;
        World world = ender.getEntityWorld();
        BlockPos pos = ender.getBlockPos();
        for (int i = 0; i < 8; i++) {
            if (world.getBlockState(pos).getFluidState().isTagged(FluidTags.LAVA))
                return true;
            if (world.getBlockState(pos).getFluidState().isTagged(FluidTags.WATER))
                return true;
            if (world.getBlockState(pos).isIn(BlockTags.FIRE))
                return true;
            if (world.getBlockState(pos).isIn(Blocks.MAGMA_BLOCK))
                return true;
            if (!world.getBlockState(pos).getShape(world, pos).isEmpty())
                return false;
            pos = pos.down();
        }
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return shouldExecute();
    }

    @Override
    public void startExecuting() {
    }

    @Override
    public void resetTask() {
    }

    @Override
    public void tick() {
        // Look down, whaaa!
        ender.getLookController().setLookPosition(ender.getX(), ender.getY(), ender.getZ());
        if(ender.getRNG().nextBoolean() && ((IEnderman) ender).randomTeleport()) {
            ender.fallDistance = 0;
        }
    }
}
