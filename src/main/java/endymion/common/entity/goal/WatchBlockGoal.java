package endymion.common.entity.goal;

import java.util.EnumSet;
import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;

public class WatchBlockGoal extends Goal {
    protected final MobEntity entity;
    protected Vector3d lookPos;
    protected Block block;
    protected BlockPos blockPos;
    protected final float range;
    private int lookTime;
    protected final float chance;
    protected final Predicate<BlockState> blockPredicate;

    public WatchBlockGoal(MobEntity entity, Predicate<BlockState> blocks, float range) {
        this(entity, blocks, range, 0.02F);
    }

    public WatchBlockGoal(MobEntity entity, Predicate<BlockState> p_i1632_2_, float range, float p_i1632_4_) {
        this.entity = entity;
        this.range = range;
        this.blockPredicate = p_i1632_2_;
        this.chance = p_i1632_4_;
        this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        if (entity.getRNG().nextFloat() >= chance) {
            return false;
        } else {
            BlockPos pos = entity.getBlockPos();
            int hgt = MathHelper.ceil(entity.getHeight());
            int r = MathHelper.ceil(range);
            for (int i = 0; i < 10 * r; i++) {
                int randX = entity.getRNG().nextInt(r * 3 / 2) - entity.getRNG().nextInt(r * 3 / 2);
                int randZ = entity.getRNG().nextInt(r * 3 / 2) - entity.getRNG().nextInt(r * 3 / 2);
                int randY = entity.getRNG().nextInt(hgt);

                BlockPos off = pos.add(randX, randY, randZ);
                BlockState state = entity.getEntityWorld().getBlockState(off);
                Vector3d offset = state.getOffset(entity.getEntityWorld(), off);
                if (blockPredicate.test(state)) {
                    Vector3d loc = new Vector3d(
                        off.getX() + 0.3 + entity.getRNG().nextDouble() * 0.4,
                        off.getY() + 0.3 + entity.getRNG().nextDouble() * 0.4,
                        off.getZ() + 0.3 + entity.getRNG().nextDouble() * 0.4
                    ).add(offset);
                    Vector3d eyes = entity.getEyePosition(1);
                    BlockRayTraceResult res =  entity.getEntityWorld().rayTraceBlocks(new RayTraceContext(
                        eyes, loc,
                        RayTraceContext.BlockMode.COLLIDER,
                        RayTraceContext.FluidMode.NONE,
                        entity
                    ));
                    if(eyes.distanceTo(loc) < range && (res.getType() == RayTraceResult.Type.MISS || res.getPos().equals(pos))) {
                        block = state.getBlock();
                        blockPos = off;
                        lookPos = res.getHitVec();
                        break;
                    }
                }
            }

            return lookPos != null;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!blockPredicate.test(entity.getEntityWorld().getBlockState(blockPos))) {
            return false;
        } else if (entity.getDistanceSq(lookPos) > range * range) {
            return false;
        } else {
            return lookTime > 0;
        }
    }

    @Override
    public void startExecuting() {
        lookTime = 40 + entity.getRNG().nextInt(40);
    }

    @Override
    public void resetTask() {
        lookPos = null;
    }

    @Override
    public void tick() {
        entity.getLookController().setLookPosition(lookPos.getX(), lookPos.getY(), lookPos.getZ());
        lookTime--;
    }
}
